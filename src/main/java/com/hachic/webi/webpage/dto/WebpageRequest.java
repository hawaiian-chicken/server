package com.hachic.webi.webpage.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * 웹 페이지 요청 DTO
 */
public record WebpageRequest(

        @NotNull
        @Schema(description = "원본 HTML")
        @JsonProperty
        String html
) {
}
