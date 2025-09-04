package com.hachic.webi.auth.application.social;

import com.hachic.webi.auth.domain.SocialLoginType;

/**
 * 모든 소셜 로그인 플랫폼에서 공통적으로 사용할 인터페이스
 * 각 플랫폼의 로그인 URL을 생성하는 메소드를 선언함
 */
public interface SocialOauth {

	/**
	 * 각 Social Login 페이지로 Redirect 처리할 URL Build
	 * 사용자로부터 로그인 요청을 받아 Social Login Server 인증용 code 요청
	 */
	String getOauthRedirectUrl(String state);

	/**
	 * API 서버로부터 받은 code를 활용하여 사용자 인증 정보를 요청
	 * @param code API 서버에서 받아온 code
	 * @return API 서버로부터 응답받은 JSON 형태의 결과를 string으로 반환
	 */
	String requestAccessToken(String code, String state);

	default SocialLoginType type() {
		return switch (this) {
			case GoogleOauth googleOauth -> SocialLoginType.GOOGLE;
			case KakaoOauth kakaoOauth -> SocialLoginType.KAKAO;
			case NaverOauth naverOauth -> SocialLoginType.NAVER;
			default -> null;
		};
	}
}
