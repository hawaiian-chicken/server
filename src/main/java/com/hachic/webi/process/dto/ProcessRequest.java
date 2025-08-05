package com.hachic.webi.process.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record ProcessRequest(
		@NotNull @Schema(description = "원본 html") @JsonProperty("webpage_id") String webpageId,
		@NotNull @Schema(description = "유저가 보내는 메시지") @JsonProperty("message") String userMessage,
		@NotNull @Schema(description = "user id") @JsonProperty("user_id") String userId) {
}
