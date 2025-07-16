package com.hachic.webi.process.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record ProcessRequest(
		@NotNull @Schema(description = "원본 html") @JsonProperty("webpage_uuid") String webpageUuid,
		@NotNull @Schema(description = "user id") @JsonProperty("user_id") String userId) {
}
