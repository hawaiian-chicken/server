package com.hachic.webi.oauth.service.social;

import org.springframework.stereotype.Component;

@Component
public class KakaoOauth implements SocialOauth {
	@Override
	public String getOauthRedirectUrl() {
		return "";
	}

	@Override
	public String requestAccessToken(String code) {
		return "";
	}
}
