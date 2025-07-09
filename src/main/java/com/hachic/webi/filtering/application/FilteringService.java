package com.hachic.webi.filtering.application;

import com.hachic.webi.filtering.dto.FilteringRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.print.attribute.standard.Media;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class FilteringService {

    public void filterHtml(FilteringRequest filteringRequest) {
        RestTemplate restTemplate = new RestTemplate();
        String aiServerUrl = "http://api.webi.click/api/process-html";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // HTML을 JSON으로 감쌈
        Map<String, String> body = new ConcurrentHashMap<>();
        body.put("html", filteringRequest.originalHtml());
        body.put("text", "주민등록등본을 발급받고 싶어");

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(aiServerUrl, request, String.class);
        String filteredHtml = response.getBody();
    }


}
