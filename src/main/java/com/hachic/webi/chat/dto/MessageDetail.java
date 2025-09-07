package com.hachic.webi.chat.dto;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hachic.webi.chat.domain.Message;
import com.hachic.webi.chat.domain.Role;

public record MessageDetail(
		@JsonProperty("message_id") String messageId,
		Role role,
		String content,
		@JsonProperty("created_at") Instant createdAt
) {
	public MessageDetail(String messageId, Role role, String content, Instant createdAt) {
		this.messageId = messageId;
		this.role = role;
		this.content = content;
		this.createdAt = createdAt;
	}

	public static MessageDetail from(Message message) {
		return new MessageDetail(
				message.getId().toString(),
				message.getRole(),
				message.getContent(),
				message.getCreatedAt()
		);
	}
}
