package com.hachic.webi.process.application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bson.types.ObjectId;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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
import com.hachic.webi.config.RabbitMqConfig;
import com.hachic.webi.process.dao.ProcessResultRepository;
import com.hachic.webi.process.domain.ProcessResult;
import com.hachic.webi.process.dto.Actions;
import com.hachic.webi.process.dto.ProcessRequest;
import com.hachic.webi.process.dto.ProcessResponse;
import com.hachic.webi.webpage.dao.WebpageRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@EnableMongoRepositories(basePackages = "com.hachic.webi")
public class ProcessService {

	private final RestTemplate restTemplate;
	private final WebpageRepository webpageRepository;
	private final ProcessResultRepository processResultRepository;
	private final RabbitTemplate rabbitTemplate;

	// AI 서버 URL 설정
	@Value("${ai.server.url}")
	private String aiServerUrl;

	/**
	 * 원본 html을 ai 서버로 보낸 후 응답을 몽고DB에 저장하고 RabbitMQ로 메시지를 전송합니다
	 * @param filteringRequest webpage_id, message, user_id
	 * @throws IOException 웹페이지 없는 경우, AI 응답 형식 벗어난 경우
	 */
	@SuppressWarnings("checkstyle:LineLength")
	public void processHtml(ProcessRequest filteringRequest) throws IOException {

		// 1. LLM 서버 요청 및 응답 처리
		// 요청 헤더 설정
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		// TODO: custom error 생성
		String originalHtml = webpageRepository
				.findHtmlById(toObejctId(filteringRequest.webpageId()))
				.orElseThrow(() ->
						new IllegalArgumentException("Webpage not found with id: " + filteringRequest.webpageId()))
				.html();

		// 요청 본문 구성 (html, userMessage 포함)
		Map<String, String> body = new ConcurrentHashMap<>();
		body.put("html", originalHtml);
		body.put("text", filteringRequest.userMessage());

		// 요청 객체 생성
		HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

		// AI 서버에 POST 요청
		ResponseEntity<String> responseEntity = restTemplate.postForEntity(aiServerUrl, request, String.class);
		String response = responseEntity.getBody();

		// 2. 응답 파싱 및 DB 저장
		// 응답에서 modified_html, message 추출
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

		// 데이터 저장
		ProcessResult processResult = new ProcessResult(
				filteringRequest.userId(), // 요청 유저 id
				filteringRequest.userMessage(), // 유저가 요청한 메시지
				toObejctId(filteringRequest.webpageId()), // 요청한 웹사이트의 id
				htmlRootNode.path("request_type").asText(), // 유청 요형 (e.g. document_service)
				//modifiedHtml, // 수정된 html
				actions,
				aiMessage
		);
		processResultRepository.save(processResult);

		// 3. RabbitMQ로 응답 전송
		ProcessResponse finalResponse = ProcessResponse.of(actions, filteringRequest.userId(), aiMessage);
		rabbitTemplate.convertAndSend(RabbitMqConfig.RESPONSE_QUEUE, finalResponse);

		log.info("Sent response via RabbitMQ for user: {}", filteringRequest.userId());
	}

//	/**
//	 * 소켓으로 수정된 html과 유저id, ai 응답 메시지를 보냅니다
//	 * @param actions action, tag, message
//	 */
//	private void sendMessage(List<Actions> actions, String aiMessage) {
//
//		// 요청 헤더 설정 (JSON 형식)
//		HttpHeaders headers = new HttpHeaders();
//		headers.setContentType(MediaType.APPLICATION_JSON);
//
//		// 요청 본문 구성 (actions 안에 action, tag, message 포함)
//		Map<String, Object> body = new ConcurrentHashMap<>();
//		body.put("actions", actions);
//		body.put("message", aiMessage);
//
//		// 요청 객체 생성
//		HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
//
//		// 소켓 서버에 POST 요청
//		ResponseEntity<String> responseEntity = restTemplate.postForEntity(socketUrl, request, String.class);
//
//		if (responseEntity.getStatusCode().is2xxSuccessful()) {
//			System.out.println(responseEntity.getBody());
//		} else {
//			// TODO: 에러처리
//			System.err.println(responseEntity.getBody());
//		}
//	}

	/**
	 * String 타입의 id를 ObjectId타입으로 바꿉니다
	 * @param id String 타입의 id
	 * @return ObjectId 타입의 id
	 */
	private ObjectId toObejctId(String id) {
		return new ObjectId(id);
	}
}
