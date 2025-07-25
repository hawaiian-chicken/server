package com.hachic.webi.process.application;

import java.io.IOException;
import java.util.Map;
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
import com.hachic.webi.process.dao.ProcessResultRepository;
import com.hachic.webi.process.domain.ProcessResult;
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
	private final ProcessResultRepository processResultRepository;

	// AI 서버 URL 설정
	@Value("${ai.server.url}")
	private String aiServerUrl;

	// 메시지 전송 대상 URL 설정
	@Value("${socket.url}")
	private String socketUrl;

	/**
	 * 원본 html을 ai 서버로 보낸 후 응답을 몽고DB에 저장하고 소켓으로 메시지를 전송합니다
	 * @param filteringRequest webpage_id, user_id
	 * @return 수정된 html과 요청한 user id
	 * @throws IOException
	 */
	public ProcessResponse processHtml(ProcessRequest filteringRequest) throws IOException {

		// 요청 헤더 설정
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		// TODO: custom error 생성
		String originalHtml = webpageRepository
				.findHtmlById(toObejctId(filteringRequest.webpageId())).orElseThrow().html();

		// 요청 본문 구성 (html, text 포함)
		Map<String, String> body = new ConcurrentHashMap<>();
		body.put("html", originalHtml);
		// TODO: text를 유저의 요구사항으로 변경
		String text = "주민등록등본을 발급받고 싶어";
		body.put("text", text);

		// 요청 객체 생성
		HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

		// AI 서버에 POST 요청
		ResponseEntity<String> responseEntity = restTemplate.postForEntity(aiServerUrl, request, String.class);
		String response = responseEntity.getBody();

		//응답에서 modified_html, message 추출
		ObjectMapper htmlMapper = new ObjectMapper();
		JsonNode htmlRootNode = htmlMapper.readTree(response).path("data");

		String modifiedHtml = htmlRootNode
				.path("modified_html")
				.asText();

		String message = htmlRootNode
				.path("message")
				.asText();

		String requestType = htmlRootNode
				.path("request_type")
				.asText();

		// 데이터 저장
		ProcessResult processResult = new ProcessResult(
				filteringRequest.userId(),
				text,
				toObejctId(filteringRequest.webpageId()),
				requestType,
				modifiedHtml,
				message
		);
		processResultRepository.save(processResult);

		// TODO: api 분리
		// 소켓으로 메시지 전송
		sendMessage(modifiedHtml, filteringRequest.userId(), message);

		return ProcessResponse.of(modifiedHtml, filteringRequest.userId());
	}

	/**
	 * 소켓으로 수정된 html과 유저id, ai 응답 메시지를 보냅니다
	 * @param filteredHtml 수정된 html
	 * @param userId 유저 id
	 * @param message ai의 응답 메시지
	 */
	private void sendMessage(String filteredHtml, String userId, String message) {

		// 요청 헤더 설정 (JSON 형식)
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		// 요청 본문 구성 (filteredHtml, userId, message 포함)
		Map<String, String> body = new ConcurrentHashMap<>();
		body.put("html", filteredHtml);
		body.put("user_id", userId);
		body.put("message", message);

		// 요청 객체 생성
		HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

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
	 * String 타입의 id를 ObjectId타입으로 바꿉니다
	 * @param id String 타입의 id
	 * @return ObjectId 타입의 id
	 */
	private ObjectId toObejctId(String id) {
		return new ObjectId(id);
	}
}

