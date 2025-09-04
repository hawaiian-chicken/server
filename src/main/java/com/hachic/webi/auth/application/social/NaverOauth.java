package com.hachic.webi.auth.application.social;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
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
	private String naverSnsBaseUrl;
	@Value("${sns.naver.client.id}")
	private String naverSnsClientId;
	@Value("${sns.naver.callback.url}")
	private String naverSnsCallbackUrl;
	@Value("${sns.naver.client.secret}")
	private String naverSnsClientSecret;
	@Value("${sns.naver.token.url}")
	private String naverSnsTokenBaseUrl;

	@Override
	public String getOauthRedirectUrl(String state) {
		return naverSnsBaseUrl
				+ "?response_type=code"
				+ "&client_id=" + naverSnsClientId
				+ "&redirect_uri=" + naverSnsCallbackUrl
				+ "&state=" + state;
	}

	@Override
	public String requestAccessToken(String code, String state) {
		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("client_id", naverSnsClientId);
		params.add("client_secret", naverSnsClientSecret);
		params.add("redirect_uri", naverSnsCallbackUrl);
		params.add("grant_type", "authorization_code");
		params.add("code", code);
		params.add("state", state); // 앞서 전송한 state 값과 일치해야 함

		// HttpEntity에 헤더와 파라미터를 함께 담음
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
		ResponseEntity<String> responseEntity = restTemplate.postForEntity(naverSnsTokenBaseUrl, request, String.class);

		if (responseEntity.getStatusCode().is2xxSuccessful()) {
			return responseEntity.getBody();
		}
		throw new IllegalStateException(
				"Naver token exchange failed " + responseEntity.getStatusCode() + " " + responseEntity.getBody());
	}

	private static String enc(String str) {
		return URLEncoder.encode(str, StandardCharsets.UTF_8);
	}
}
