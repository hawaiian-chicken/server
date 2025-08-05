package com.hachic.webi.process.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

public record ProcessResponse(
		@Schema(description = "필터링된 actions 리스트(action, tag, message)")@JsonProperty("actions") List<Actions> actions,
		@Schema(description = "user id") @JsonProperty("user_id") String userId) {

	public static ProcessResponse of(List<Actions> actions, String userId) {
		return new ProcessResponse(actions, userId);
	}
}
