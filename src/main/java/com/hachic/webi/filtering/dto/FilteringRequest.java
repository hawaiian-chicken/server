package com.hachic.webi.filtering.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record FilteringRequest(
        @NotNull @Schema(description = "원본 html") String originalHtml,
        @NotNull @Schema(description = "user id") @JsonProperty("user_id") String userId) {
}
