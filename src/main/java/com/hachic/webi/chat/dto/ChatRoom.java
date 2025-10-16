package com.hachic.webi.chat.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

public record ChatRoom(
		@Schema(description = "채팅방 ID")
		@JsonProperty("conversation_id") Long conversationId
) {
	public static  ChatRoom of(Long conversationId) {
		return new ChatRoom(conversationId);
	}
}
