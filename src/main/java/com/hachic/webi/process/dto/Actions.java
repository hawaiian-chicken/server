package com.hachic.webi.process.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record Actions(
		@NotNull @Schema(description = "action(click, remove, highlight 등)") String action,
		@NotNull @Schema(description = "button#가족관계증명서 (같은 태그를 특정할 수 있는 무언가, xPath 값 같은 것") String tag,
		@NotNull @Schema(description = "ai 응답 메시지") @JsonProperty("message") String aiMessage) {

	public static Actions of(String action, String tag, String aiMessage) {
		return new Actions(action, tag, aiMessage);
	}
}
