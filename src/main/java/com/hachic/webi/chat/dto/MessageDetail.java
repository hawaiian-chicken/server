package com.hachic.webi.chat.dto;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hachic.webi.chat.domain.Message;
import com.hachic.webi.chat.domain.Role;

import io.swagger.v3.oas.annotations.media.Schema;

public record MessageDetail(
		@Schema(description = "메시지 ID") @JsonProperty("message_id") String messageId,
		@Schema(description = "USER or AI") Role role,
		@Schema(description = "메시지 내용") String content,
		@Schema(description = "메시지 전송 시각") @JsonProperty("created_at") Instant createdAt
) {
	public static MessageDetail from(Message message) {
		return new MessageDetail(
				message.getId().toString(),
				message.getRole(),
				message.getContent(),
				message.getCreatedAt()
		);
	}
}
