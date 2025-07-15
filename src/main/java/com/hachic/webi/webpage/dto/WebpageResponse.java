package com.hachic.webi.webpage.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 웹 페이지 응답 DTO
 */
public record WebpageResponse(

        @Schema(description = "요청 고유 ID")
        @JsonProperty("user_id")
        String userId
) {
}
