package com.hachic.webi.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserResponse(
		@Schema(description = "유저 이름") String name,
		@Schema(description = "Social ID") @JsonProperty("social_id") String socialId,
		@Schema(description = "Google/Kakao/Naver") String provider
) {
	public static UserResponse of(String name, String socialId, String provider) {
		return new UserResponse(name, socialId, provider);
	}
}
