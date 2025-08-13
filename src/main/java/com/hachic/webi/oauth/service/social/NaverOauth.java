package com.hachic.webi.oauth.service.social;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
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
	public String getOauthRedirectUrl() {
		Map<String, Object> params = new HashMap<>();
		params.put("response_type", "code");
		params.put("client_id", naverSnsClientId);
		params.put("redirect_uri", naverSnsCallbackUrl);
		params.put("state", "random_state_string"); // CSRF 방지

		String parameterString = params.entrySet().stream()
				.map(x -> x.getKey() + "=" + x.getValue())
				.collect(Collectors.joining("&"));

		return naverSnsBaseUrl + "?" + parameterString;
	}

	@Override
	public String requestAccessToken(String code) {
		RestTemplate restTemplate = new RestTemplate();

		Map<String, Object> params = new HashMap<>();
		params.put("code", code);
		params.put("client_id", naverSnsClientId);
		params.put("client_secret", naverSnsClientSecret);
		params.put("redirect_uri", naverSnsCallbackUrl);
		params.put("grant_type", "authorization_code");
		params.put("state", "random_state_string"); // 앞서 전송한 state 값과 일치해야 함

		ResponseEntity<String> responseEntity = restTemplate.postForEntity(naverSnsTokenBaseUrl, params, String.class);

		if (responseEntity.getStatusCode() == HttpStatus.OK) {
			String tokenResponse = responseEntity.getBody();
			System.out.println(tokenResponse);

			return responseEntity.getBody();
		}
		return "NAVER 로그인 요청 처리 실패";
	}

	public String requestAccessTokenUsingUrl(String code) {
		try {
			URL url = new URL(naverSnsTokenBaseUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.setDoOutput(true);

			Map<String, Object> params = new HashMap<>();
			params.put("code", code);
			params.put("client_id", naverSnsClientId);
			params.put("client_secret", naverSnsClientSecret);
			params.put("redirect_uri", naverSnsCallbackUrl);
			params.put("grant_type", "authorization_code");
			params.put("state", "random_state_string");

			String parameterString = params.entrySet().stream()
					.map(x -> x.getKey() + '=' + x.getValue())
					.collect(Collectors.joining("&"));

			BufferedOutputStream bous = new BufferedOutputStream(connection.getOutputStream());
			bous.write(parameterString.getBytes());
			bous.flush();
			bous.close();

			BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

			StringBuilder sb = new StringBuilder();
			String line;

			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

			if (connection.getResponseCode() == 200) {
				String tokenResponse = sb.toString();
				System.out.println("Received Access Token: " + tokenResponse);

				return tokenResponse;
			}
			return "NAVER 로그인 요청 처리 실패";
		} catch (IOException e) {
			throw new IllegalArgumentException(
					"알 수 없는 NAVER 로그인 Access Token 요청 URL 입니다 :: " + naverSnsTokenBaseUrl);
		}
	}
}
