package com.hachic.webi.chat.dto;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hachic.webi.chat.domain.Conversation;

import io.swagger.v3.oas.annotations.media.Schema;

public record ConversationDetail(
		@Schema(description = "채팅방 ID") Long id,
		@Schema(description = "채팅방 제목") String title,
		@Schema(description = "채팅방 관련 태그들") List<String> tags,
		@Schema(description = "최근 메시지 전송 시각") @JsonProperty("last_message_at") Instant lastMessageAt,
		@Schema(description = "채팅방의 메시지 수") @JsonProperty("message_count") int msgCount
) {
	public ConversationDetail(Long id, String title, List<String> tags, Instant lastMessageAt, int msgCount) {
		this.id = id;
		this.title = title;
		this.tags = tags;
		this.lastMessageAt = lastMessageAt;
		this.msgCount = msgCount;
	}

	public static ConversationDetail from(Conversation conversation) {
		return new ConversationDetail(
				conversation.getId(),
				conversation.getTitle(),
				conversation.getTags(),
				conversation.getLastMessageAt(),
				conversation.getMsgCount()
		);
	}
}
