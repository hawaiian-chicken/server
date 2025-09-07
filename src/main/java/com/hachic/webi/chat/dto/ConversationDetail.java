package com.hachic.webi.chat.dto;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hachic.webi.chat.domain.Conversation;

public record ConversationDetail(
		Long id,
		String title,
		List<String> tags,
		@JsonProperty("last_message_at") Instant lastMessageAt,
		@JsonProperty("message_count") int msgCount
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
