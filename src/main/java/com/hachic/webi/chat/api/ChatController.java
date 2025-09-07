package com.hachic.webi.chat.api;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hachic.webi.chat.application.ChatService;
import com.hachic.webi.chat.dto.ChatRoom;
import com.hachic.webi.chat.dto.ConversationDetail;
import com.hachic.webi.chat.dto.MessageDetail;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@Tag(name = "Chat API", description = "채팅방 관련 API")
@RequestMapping("/chat")
public class ChatController {

	private final ChatService chatService;

	@PostMapping("/chat-room")
	@Operation(summary = "채팅방을 생성하고 채팅방 ID를 반환하는 API")
	public ResponseEntity<ChatRoom> createChatRoom(
			// @AuthenticationPrincipal OAuth2ResourceServerProperties.Jwt jwt
	) {
		// TODO: 로그인 구현 후 userId 변경
		// String userId = jwt.getSubject();
		String userId = "hachic";
		return ResponseEntity.ok(chatService.createConversation(userId));
	}

	@GetMapping()
	@Operation(summary = "현재 로그인된 유저의 채팅방 목록을 보여주는 API")
	public ResponseEntity<List<ConversationDetail>> getConversations(
			// @AuthenticationPrincipal OAuth2ResourceServerProperties.Jwt jwt
	) {
		// TODO: 로그인 구현 후 userId 변경
		// String userId = jwt.getSubject();
		String userId = "hachic";
		return ResponseEntity.ok(chatService.getConversations(userId));
	}

	@GetMapping("/{conversation_id}")
	@Operation(summary = "특정 채팅방의 상세 대화 내역을 보여주는 API")
	public ResponseEntity<List<MessageDetail>> getMessages(
			@PathVariable("conversation_id") Long conversationId
			// @AuthenticationPrincipal OAuth2ResourceServerProperties.Jwt jwt
	) {
		// TODO: 로그인 구현 후 userId가 conversation에 저장된 id와 같을 때만 접근 허용
		return ResponseEntity.ok(chatService.getMessages(conversationId));
	}
}
