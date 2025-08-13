package com.hachic.webi.oauth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserResponse(
		@Schema(description = "유저 이름") String name,
		@Schema(description = "Access Token") String accessToken,
		@Schema(description = "Google/Kakao/Naver") String provider
) {
	public static UserResponse of(String name, String accessToken, String provider) {
		return new UserResponse(name, accessToken, provider);
	}
}
