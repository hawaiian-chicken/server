package com.hachic.webi.chat.dto;

import com.hachic.webi.chat.domain.Message;
import com.hachic.webi.chat.domain.Role;

import io.swagger.v3.oas.annotations.media.Schema;

public record MessageHistory(
		@Schema(description = "USER | AI") Role role,
		@Schema(description = "대화 내용") String content
) {
	public static MessageHistory from(Message msg) {
		if (msg.getRole() == Role.AI) {
			return new MessageHistory(Role.ASSISTANT, msg.getContent());
		}
		return new MessageHistory(msg.getRole(), msg.getContent());
	}
}
