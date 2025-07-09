package com.hachic.webi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record WebpageRequest(

        @NotNull
        @Schema(description = "원본 HTML")
        @JsonProperty
        String html
) {}
