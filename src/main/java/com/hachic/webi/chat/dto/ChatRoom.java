package com.hachic.webi.chat.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ChatRoom(
		@JsonProperty("conversation_id") Long conversationId
) {
	public static  ChatRoom of(Long conversationId) {
		return new ChatRoom(conversationId);
	}
}
