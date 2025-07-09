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

    public ProcessResponse filterHtml(ProcessRequest filteringRequest) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        String aiServerUrl = "http://ai.webi.click:80/api/process-html";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // HTML을 JSON으로 감쌈
        Map<String, String> body = new ConcurrentHashMap<>();
        body.put("html", filteringRequest.originalHtml());
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

        return ProcessResponse.of(filteredHtml, filteringRequest.userId());
    }


}
