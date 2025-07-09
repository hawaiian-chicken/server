package com.hachic.webi.filtering.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

public record FilteringResponse(
        @Schema(description = "필터링된 html string") @JsonProperty("html") String filtered_html,
        @Schema(description = "user id") @JsonProperty("user_id") String userId) {
}
