package com.hachic.webi.oauth.service;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hachic.webi.oauth.helper.constants.SocialLoginType;
import com.hachic.webi.oauth.service.social.SocialOauth;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OauthService {
	private final List<SocialOauth> socialOauthList;
	private final HttpServletResponse response;

	/**
	 * 사용자가 특정 소셜 로그인 타입(GOOGLE, KAKAO, NAVER)을 선택했을 때,
	 * 해당 소셜 서비스의 인증 페이지로 리다이렉션하는 역할
	 * @param socialLoginType 소셜 로그인 타입
	 */
	public void request(SocialLoginType socialLoginType) {
		SocialOauth socialOauth = this.findSocialOauthByType(socialLoginType);
		String redirectUrl = socialOauth.getOauthRedirectUrl();
		try {
			response.sendRedirect(redirectUrl);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 사용자가 소셜 서비스에서 로그인을 완료한 후, 소셜 서비스로부터 받은 인가 코드를 사용해 access token을 요청하는 역할
	 * @param socialLoginType 소셜 로그인 타입
	 * @param code authorization code
	 * @return 발급받은 액세스 토큰 반환
	 */
	public String requestAccessToken(SocialLoginType socialLoginType, String code) {
		SocialOauth socialOauth = this.findSocialOauthByType(socialLoginType);
		return socialOauth.requestAccessToken(code);
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
