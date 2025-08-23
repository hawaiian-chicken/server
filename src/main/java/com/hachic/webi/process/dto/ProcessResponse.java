package com.hachic.webi.process.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

public record ProcessResponse(
		@Schema(description = "필터링된 actions 리스트(action, tag, message)")@JsonProperty("actions") List<Actions> actions,
		@Schema(description = "user id") @JsonProperty("user_id") String userId,
		@Schema(description = "사용자에게 보여줄 AI 응답 메시지") @JsonProperty("message") String aiMessage) {

	public static ProcessResponse of(List<Actions> actions, String userId, String aiMessage) {
		return new ProcessResponse(actions, userId, aiMessage);
	}
}
