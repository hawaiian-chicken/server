package com.hachic.webi.chat.api;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hachic.webi.chat.application.ChatService;
import com.hachic.webi.chat.dto.ChatRoom;
import com.hachic.webi.chat.dto.ConversationResponse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/chat")
public class ChatController {

	private final ChatService chatService;

	@PostMapping("/chat-room")
	public ResponseEntity<ChatRoom> createChatRoom(
			// @AuthenticationPrincipal OAuth2ResourceServerProperties.Jwt jwt
	) {
		// String userId = jwt.getSubject();
		String userId = "hachic";
		return ResponseEntity.ok(chatService.createConversation(userId));
	}

	@GetMapping()
	public ResponseEntity<List<ConversationResponse>> getConversations(
			// @AuthenticationPrincipal OAuth2ResourceServerProperties.Jwt jwt
	) {
		// String userId = jwt.getSubject();
		String userId = "hachic";
		return ResponseEntity.ok(chatService.getConversations(userId));
	}
}
