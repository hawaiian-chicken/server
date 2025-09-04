package com.hachic.webi.auth.application.social;

import org.springframework.stereotype.Component;

@Component
public class GoogleOauth implements SocialOauth {
	@Override
	public String getOauthRedirectUrl(String state) {
		return "";
	}

	@Override
	public String requestAccessToken(String code, String state) {
		return "";
	}
}
