package com.hachic.webi.process.application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hachic.webi.chat.dao.ConversationRepository;
import com.hachic.webi.chat.dao.MessageRepository;
import com.hachic.webi.chat.domain.Conversation;
import com.hachic.webi.chat.domain.Message;
import com.hachic.webi.chat.domain.Role;
import com.hachic.webi.process.dto.Actions;
import com.hachic.webi.process.dto.ProcessRequest;
import com.hachic.webi.process.dto.ProcessResponse;
import com.hachic.webi.webpage.dao.WebpageRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@EnableMongoRepositories(basePackages = "com.hachic.webi")
public class ProcessService {

	private final RestTemplate restTemplate;
	private final WebpageRepository webpageRepository;
	private final ConversationRepository conversationRepository;
	private final MessageRepository messageRepository;

	// AI 서버 URL 설정
	@Value("${ai.server.url}")
	private String aiServerUrl;

	// 메시지 전송 대상 URL 설정
	@Value("${socket.url}")
	private String socketUrl;

	/**
	 * 원본 html을 ai 서버로 보낸 후 응답을 몽고DB에 저장하고 소켓으로 메시지를 전송합니다
	 * @param filteringRequest webpage_iqqd, message, user_id
	 * @return 수정된 html과 요청한 user id
	 * @throws IOException
	 */
	public ProcessResponse processHtml(ProcessRequest filteringRequest, String userId) throws IOException {

		// 유저가 보낸 메시지 저장
		saveMessage(filteringRequest.conversationId(), userId, Role.USER, filteringRequest.userMessage());

		// 요청 헤더 설정
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		// TODO: custom error 생성
		String originalHtml = webpageRepository
				.findHtmlById(toObjectId(filteringRequest.webpageId())).orElseThrow().html();

		// 요청 본문 구성 (html, userMessage 포함)
		Map<String, String> body = new ConcurrentHashMap<>();
		body.put("html", originalHtml);
		body.put("text", filteringRequest.userMessage());

		// 요청 객체 생성
		HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

		// AI 서버에 POST 요청
		ResponseEntity<String> responseEntity = restTemplate.postForEntity(aiServerUrl, request, String.class);
		String response = responseEntity.getBody();

		// 응답에서 actions, message 추출
		ObjectMapper htmlMapper = new ObjectMapper();
		JsonNode htmlRootNode = htmlMapper.readTree(response).path("data");

		JsonNode actionsNode = htmlRootNode.path("actions");
		List<Actions> actions = new ArrayList<>();
		if (actionsNode != null && actionsNode.isArray()) {
			for (JsonNode actionNode : actionsNode) {
				actions.add(Actions.of(
						actionNode.path("action").asText(),
						actionNode.path("tag").asText(),
						actionNode.path("message").asText()
				));
			}
		}
		String aiMessage = htmlRootNode
				.path("message")
				.asText();

		// AI 응답 메시지 저장
		saveMessage(filteringRequest.conversationId(), userId, Role.ASSISTANT, aiMessage);

		// 소켓으로 메시지 전송
		// TODO: chat 404 NOT FOUNT 에러 해결
		// sendMessage(actions, aiMessage, userId);

		return ProcessResponse.of(actions, userId, aiMessage);
	}

	/**
	 * 소켓으로 수정된 html과 유저id, ai 응답 메시지를 보냅니다
	 * @param actions action, tag, message
	 */
	private void sendMessage(List<Actions> actions, String aiMessage, String userId) {

		// 요청 헤더 설정 (JSON 형식)
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		// 요청 본문 구성 (actions 안에 action, tag, message 포함)
		Map<String, Object> body = new ConcurrentHashMap<>();
		body.put("actions", actions);
		body.put("message", aiMessage);

		// 요청 객체 생성
		HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

		socketUrl += "/" + userId + "/message";
		// 소켓 서버에 POST 요청
		ResponseEntity<String> responseEntity = restTemplate.postForEntity(socketUrl, request, String.class);

		if (responseEntity.getStatusCode().is2xxSuccessful()) {
			System.out.println(responseEntity.getBody());
		} else {
			// TODO: 에러처리
			System.err.println(responseEntity.getBody());
		}
	}

	/**
	 * Message Document를 저장하는 메서드
	 * @param conversationId 채팅방 번호
	 * @param userId 유저의 social ID
	 * @param role USER | ASSISTANT
	 * @param content 메시지 내용
	 */
	private void saveMessage(Long conversationId, String userId, Role role, String content) {
		Conversation conv = conversationRepository.findById(conversationId)
				.orElseThrow(NoSuchElementException::new);

		// 채팅방의 첫 대화이면 ai 서버를 통해 title과 tag 추출
		if (role == Role.USER && conv.getMsgCount() == 0) {
			// TODO: ai 서버에 title과 tag를 추출하는 메서드 호출
			String title = "title";
			List<String> tags = new ArrayList<>();
			tags.add( "tag1");
			tags.add( "tag2");
			conv.setTitleAndTags(title, tags);
			conversationRepository.save(conv);
		}

		// Message 저장
		Message msg = new Message(
				conversationId,
				userId,
				role,
				content
		);
		messageRepository.save(msg);
		conv.addMessage(messageRepository.countByConversationId(conversationId));
		conversationRepository.save(conv);
	}

	/**
	 * String 타입의 id를 ObjectId타입으로 바꿉니다
	 * @param id String 타입의 id
	 * @return ObjectId 타입의 id
	 */
	private ObjectId toObjectId(String id) {
		return new ObjectId(id);
	}
}

