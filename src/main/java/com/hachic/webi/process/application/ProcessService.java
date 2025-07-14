package com.hachic.webi.process.application;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hachic.webi.process.dto.ProcessRequest;
import com.hachic.webi.process.dto.ProcessResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProcessService {

	public ProcessResponse processHtml(ProcessRequest filteringRequest) throws IOException {

		// AI 서버 URL 설정
		String aiServerUrl = "http://ai.webi.click:80/api/process-html";

		// 요청 헤더 설정
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		// 요청 본문 구성 (html, text 포함)
		Map<String, String> body = new ConcurrentHashMap<>();
		body.put("html", filteringRequest.originalHtml());
		// TODO: text를 유저의 요구사항으로 변경
		body.put("text", "주민등록등본을 발급받고 싶어");

		// 요청 객체 생성
		HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

		// AI 서버에 POST 요청
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> responseEntity = restTemplate.postForEntity(aiServerUrl, request, String.class);
		String response = responseEntity.getBody();

		//응답에서 modified_html, message 추출
		ObjectMapper htmlMapper = new ObjectMapper();
		JsonNode htmlRootNode = htmlMapper.readTree(response);

		String filteredHtml = htmlRootNode
				.path("data")
				.path("modified_html")
				.asText();

		String message = htmlRootNode
				.path("data")
				.path("message")
				.asText();

		// TODO: api 분리
		// 소켓으로 메시지 전송
		sendMessage(filteredHtml, filteringRequest.userId(), message);

		return ProcessResponse.of(filteredHtml, filteringRequest.userId());
	}

	private void sendMessage(String filteredHtml, String userId, String message) {

		// 메시지 전송 대상 URL 설정
		String socketUrl = "http://chat.webi.click:80/users/:userId/message";

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
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> responseEntity = restTemplate.postForEntity(socketUrl, request, String.class);

		if (responseEntity.getStatusCode().is2xxSuccessful()) {
			System.out.println(responseEntity.getBody());
		} else {
			// TODO: 에러처리
			System.err.println(responseEntity.getBody());
		}
	}
}

