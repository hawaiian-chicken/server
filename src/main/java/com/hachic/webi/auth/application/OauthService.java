package com.hachic.webi.auth.application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hachic.webi.auth.application.social.SocialOauth;
import com.hachic.webi.auth.dao.UserRepository;
import com.hachic.webi.auth.domain.SocialLoginType;
import com.hachic.webi.auth.domain.User;
import com.hachic.webi.auth.dto.UserResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OauthService {
	private final List<SocialOauth> socialOauthList;
	private final UserRepository userRepository;
	private static final Logger logger = LoggerFactory.getLogger(OauthService.class);

	/**
	 * 사용자가 특정 소셜 로그인 타입(GOOGLE, KAKAO, NAVER)을 선택했을 때,
	 * 해당 소셜 서비스의 인증 페이지로 리다이렉션하는 역할
	 * @param socialLoginType 소셜 로그인 타입
	 * @return 소셜 로그인 요청 URL 반환
	 */
	public String request(SocialLoginType socialLoginType, String state) {
		SocialOauth socialOauth = this.findSocialOauthByType(socialLoginType);
		return socialOauth.getOauthRedirectUrl(state);
	}

	/**
	 * 사용자가 소셜 서비스에서 로그인을 완료한 후, 소셜 서비스로부터 받은 인가 코드를 사용해 access token을 요청하는 역할
	 * @param socialLoginType 소셜 로그인 타입
	 * @param code authorization code
	 * @return 발급받은 액세스 토큰 반환
	 */
	public String requestAccessToken(SocialLoginType socialLoginType, String code, String state) {
		SocialOauth socialOauth = this.findSocialOauthByType(socialLoginType);
		return socialOauth.requestAccessToken(code, state);
	}

	/**
	 * JSON에서 Access Token만 추출
	 * @param accessTokenJson Access Token이 포함된 JSON
	 * @return 추출된 Access Token
	 */
	private String extractAccessTokenFromJson(String accessTokenJson) {
		// JSON을 파싱하여 Access Token만 추출
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode jsonNode = objectMapper.readTree(accessTokenJson);
			if (jsonNode.get("access_token") != null) {
				return jsonNode.get("access_token").asText();
			} else {
				return null;
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		}
	}

	public UserResponse requestAccessTokenAndSaveUser(SocialLoginType socialLoginType, String code, String state)
			throws JsonProcessingException {
		logger.info("Social Login Type & code : {} \n & \n{} \n********", socialLoginType, code);
		// 1. Access Token을 포함한 JSON 응답 요청
		String accessTokenJson = this.requestAccessToken(socialLoginType, code, state);
		logger.info("\n Access Token Json : {} \n********", accessTokenJson);
		// 2. JSON에서 Access Token만 추출
		String accessToken = extractAccessTokenFromJson(accessTokenJson);

		if (accessToken == null) {
			// 액세스 토큰이 없으면 예외 처리
			throw new RuntimeException("Failed to extract access token");
		}

		// 3. 액세스 토큰을 사용해 사용자 정보 요청
		String userInfo = getUserInfo(socialLoginType, accessToken);
		System.out.println("User Info : " + userInfo);

		// 4. 사용자 정보를 파싱하여 User 객체 생성
		// TODO: User 도메인에 맞게 수정하기
		User user = parseUserInfo(userInfo, socialLoginType, accessToken);

		// 5. 기존 사용자 확인 후 처리
		Optional<User> existingUser = userRepository.findBySocialId(user.getSocialId());
		if (existingUser.isPresent()) {
			User existing = existingUser.get();
			return UserResponse.of(existing.getName(), existing.getSocialId(), existing.getProvider());
		} else {
			// 신규 사용자 저장
			userRepository.save(user);
			return UserResponse.of(user.getName(), user.getSocialId(), user.getProvider());
		}
	}

	private String getUserInfo(SocialLoginType socialLoginType, String accessToken) {
		switch (socialLoginType) {
			case GOOGLE:
				return googleApiCall(accessToken);
			case KAKAO:
				return kakaoApiCall(accessToken);
			case NAVER:
				return naverApiCall(accessToken);
			default:
				throw new IllegalArgumentException("지원되지 않는 소셜 로그인 타입입니다.");
		}
	}

	// TODO: GOOGLE 소셜 로그인 구현
	private String googleApiCall(String accessToken) {
		return "";
	}

	// TODO: KAKAO 소셜 로그인 구현
	private String kakaoApiCall(String accessToken) {
		return "";
	}

	private String naverApiCall(String accessToken) {
		try {
			String url = "https://openapi.naver.com/v1/nid/me";
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");

			// Authorization Header에 "Bearer" 토큰 설정
			con.setRequestProperty("Authorization", "Bearer " + accessToken);

			int responseCode = con.getResponseCode();
			if (responseCode == 200) {
				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
				return response.toString();
			} else {
				throw new RuntimeException("Naver API에서 사용자 정보를 가져오는 데 실패했습니다. 응답 코드: " + responseCode);
			}

		} catch (IOException e) {
			throw new RuntimeException("Naver API 호출 중 오류 발생", e);
		}
	}

	/**
	 * 사용자 정보를 파싱하여 User 객체를 생성하는 메서드
	 * @param userInfo 유저 정보
	 * @param socialLoginType 소셜 로그인 타입
	 * @param accessToken Access Token
	 * @return 유저 객체
	 */
	private User parseUserInfo(String userInfo, SocialLoginType socialLoginType, String accessToken)
			throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode userInfoJson = objectMapper.readTree(userInfo);

		String socialId = "";
		String name = "";

		if (socialLoginType == SocialLoginType.GOOGLE) {
			// TODO: GOOGLE 구현 후 수정
			socialId = "google";
			name = "google";
		} else if (socialLoginType == SocialLoginType.KAKAO) {
			// TODO: KAKAO 구현 후 수정
			socialId = "kakao";
			name = "kakao";
		} else if (socialLoginType == SocialLoginType.NAVER) {
			JsonNode naverResponse = userInfoJson.path("response");
			socialId = naverResponse.path("id").asText();
			name = naverResponse.path("name").asText();
		}

		return new User(socialId, name, socialLoginType.name(), accessToken);
	}

	/**
	 * 파라미터로 받은 소셜 로그인 타입에 맞는 SocialOauth 객체를 찾아오는 역할
	 * @param socialLoginType 소셜 로그인 타입
	 * @return 소셜 로그인 타입과 일치하는 객체 반환
	 */
	private SocialOauth findSocialOauthByType(SocialLoginType socialLoginType) {
		return socialOauthList.stream()
				// SocialOauth 객체 리스트에서 입력받은 socialLoginType과 일치하는 객체를 찾음
				.filter(x -> x.type() == socialLoginType)
				// 조건에 맞는 객체를 찾으면 가장 먼저 발견된 객체를 반환
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("알 수 없는 SocialLoginType 입니다."));
	}
}
