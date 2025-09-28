package com.hachic.webi.oauth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserInfo(
		@Schema(description = "유저 이름") String name,
		@Schema(description = "Access Token") String accessToken,
		@Schema(description = "소셜 로그인 Provider") String provider
) {
	public static UserInfo of(String name, String accessToken, String provider) {
		return new UserInfo(name, accessToken, provider);
	}
}
