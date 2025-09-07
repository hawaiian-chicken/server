package com.hachic.webi.chat.application;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hachic.webi.chat.dao.ConversationRepository;
import com.hachic.webi.chat.dao.MessageRepository;
import com.hachic.webi.chat.domain.Conversation;
import com.hachic.webi.chat.dto.ChatRoom;
import com.hachic.webi.chat.dto.ConversationResponse;
import com.hachic.webi.oauth.dao.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatService {

	private final ConversationRepository conversationRepository;
	private final MessageRepository messageRepository;
	private final UserRepository userRepository;

	// TODO: userId -> jwt에서 유저 정보 가져오기
	public ChatRoom createConversation(String userId) {
		Conversation conversation = new Conversation(userId);
		conversationRepository.save(conversation);
		return ChatRoom.of(conversation.getId());
	}

	public List<ConversationResponse> getConversations(String userId) {
		return conversationRepository.findAllByUserId(userId)
				.stream().map(ConversationResponse::from).toList();
	}
}
