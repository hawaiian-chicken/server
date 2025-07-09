package com.hachic.webi.process.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

public record ProcessResponse(
        @Schema(description = "필터링된 html string") @JsonProperty("html") String filtered_html,
        @Schema(description = "user id") @JsonProperty("user_id") String userId) {

    public static ProcessResponse of(String filtered_html, String userId) {
        return new ProcessResponse(filtered_html, userId);
    }
}
