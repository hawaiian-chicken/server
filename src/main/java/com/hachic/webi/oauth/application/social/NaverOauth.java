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

@Component
@RequiredArgsConstructor
public class NaverOauth implements SocialOauth {
	@Value("${sns.naver.url}")
	private String naverSnsUrl;
	@Value("${sns.naver.client.id}")
	private String naverSnsClientId;
	@Value("${sns.naver.callback.url}")
	private String naverSnsCallbackUrl;
	@Value("${sns.naver.client.secret}")
	private String naverSnsClientSecret;
	@Value("${sns.naver.token.url}")
	private String naverSnsTokenUrl;

	@Override
	public String getOauthRedirectUrl() {
		Map<String, Object> params = new HashMap<>();
		params.put("response_type", "code");
		params.put("client_id", naverSnsClientId);
		params.put("redirect_uri", naverSnsCallbackUrl);
		params.put("state", "random_state_string"); // CSRF 방지

		String parameterString = params.entrySet().stream()
				.map(x -> x.getKey() + "=" + x.getValue())
				.collect(Collectors.joining("&"));

		return naverSnsUrl + "?" + parameterString;
	}

	@Override
	public String requestAccessToken(String code) {
		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("code", code);
		params.add("client_id", naverSnsClientId);
		params.add("client_secret", naverSnsClientSecret);
		params.add("redirect_uri", naverSnsCallbackUrl);
		params.add("grant_type", "authorization_code");
		params.add("state", "random_state_string"); // 앞서 전송한 state 값과 일치해야 함

		// HttpEntity에 헤더와 파라미터를 함께 담음
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

		ResponseEntity<String> responseEntity = restTemplate.postForEntity(naverSnsTokenUrl, request, String.class);

		if (responseEntity.getStatusCode() == HttpStatus.OK) {
			String tokenResponse = responseEntity.getBody();
			System.out.println(tokenResponse);

			return responseEntity.getBody();
		}
		return "NAVER 로그인 요청 처리 실패";
	}
}
