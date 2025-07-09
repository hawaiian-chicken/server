package com.hachic.webi.process.application;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hachic.webi.process.dto.ProcessRequest;
import com.hachic.webi.process.dto.ProcessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class ProcessService {

    public ProcessResponse processHtml(ProcessRequest filteringRequest) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        String aiServerUrl = "http://ai.webi.click:80/api/process-html";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // HTML을 JSON으로 감쌈
        Map<String, String> body = new ConcurrentHashMap<>();
        body.put("html", filteringRequest.originalHtml());
        // TODO: text를 유저의 요구사항으로 변경
        body.put("text", "주민등록등본을 발급받고 싶어");

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(aiServerUrl, request, String.class);
        String response = responseEntity.getBody();

        // response 중 filtered Html만 추출
        ObjectMapper htmlMapper = new ObjectMapper();
        JsonNode htmlRootNode = htmlMapper.readTree(response);

        String filteredHtml = htmlRootNode
                .path("data")
                .path("modified_html")
                .asText();

        String message = htmlRootNode
                .path("data")
                .path("mesage")
                .asText();

        // TODO: api 분리
        sendMessage(filteredHtml, filteringRequest.userId(), message);

        return ProcessResponse.of(filteredHtml, filteringRequest.userId());
    }

    private void sendMessage(String filteredHtml, String userId, String message) {
        RestTemplate restTemplate = new RestTemplate();
        String socketUrl = "http://chat.webi.click:3000/users/" + userId + "/message";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> body = new ConcurrentHashMap<>();
        body.put("html", filteredHtml);
        body.put("user_id", userId);
        body.put("message", message);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(socketUrl, request, String.class);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            System.out.println(responseEntity.getBody());
        } else {
            // TODO: 에러처리
            System.err.println(responseEntity.getBody());
        }
    }
}

