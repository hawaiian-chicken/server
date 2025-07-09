package com.hachic.webi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

public record WebpageResponse(

        @Schema(description = "요청 고유 ID")
        @JsonProperty("uuid")
        String uuid
) {}
