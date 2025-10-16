package com.hachic.webi.oauth.application.social;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoOauth implements SocialOauth {
	@Value("${sns.kakao.url}")
	private String kakaoUrl;
	@Value("${sns.kakao.client.id}")
	private String kakaoClientId;
	@Value("${sns.kakao.callback.url}")
	private String kakaoCallbackUrl;
	@Value("${sns.kakao.token.url}")
	private String kakaoTokenUrl;

	@Override
	public String getOauthRedirectUrl() {
		Map<String, Object> params = new HashMap<>();
		params.put("response_type", "code");
		params.put("client_id", kakaoClientId);
		params.put("redirect_uri", kakaoCallbackUrl);

		String paramStr = params.entrySet().stream()
				.map(x -> x.getKey() + "=" + x.getValue())
				.collect(Collectors.joining("&"));

		return kakaoUrl + "?" + paramStr;
	}

	@Override
	public String requestAccessToken(String code) {
		RestTemplate restTemplate = new RestTemplate();

		// HTTP 헤더 설정
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		// 요청 파라미터를 문자열로 인코딩
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "authorization_code");
		params.add("client_id", kakaoClientId);
		params.add("redirect_uri", kakaoCallbackUrl);
		params.add("code", code);

		// HttpEntity 생성
		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);

		// POST 요청
		ResponseEntity<String> responseEntity = restTemplate.postForEntity(kakaoTokenUrl, requestEntity, String.class);

		if (responseEntity.getStatusCode() == HttpStatus.OK) {
			String tokenResponse = responseEntity.getBody();
			System.out.println("Received Access Token");

			return responseEntity.getBody();
		}
		return String.format(
				"Failed to process Kakao login request. Status code: %s, Response Body: %s",
				responseEntity.getStatusCode(), responseEntity.getBody()
		);
	}
}
