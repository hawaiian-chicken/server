package com.hachic.webi.chat.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hachic.webi.chat.application.ChatService;
import com.hachic.webi.chat.dto.ChatRoom;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/chat")
public class ChatController {

	private final ChatService chatService;

	@PostMapping()
	public ResponseEntity<ChatRoom> createChatRoom(
			// @AuthenticationPrincipal OAuth2ResourceServerProperties.Jwt jwt
	) {
		// String userId = jwt.getSubject();
		String userId = "yub";
		return ResponseEntity.ok(chatService.createConversation(userId));
	}
}
