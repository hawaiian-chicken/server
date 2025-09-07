package com.hachic.webi.chat.application;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hachic.webi.chat.dao.ConversationRepository;
import com.hachic.webi.chat.dao.MessageRepository;
import com.hachic.webi.chat.domain.Conversation;
import com.hachic.webi.chat.dto.ChatRoom;
import com.hachic.webi.chat.dto.ConversationDetail;
import com.hachic.webi.chat.dto.MessageDetail;
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

	public List<ConversationDetail> getConversations(String userId) {
		return conversationRepository.findAllByUserId(userId)
				.stream().map(ConversationDetail::from).toList();
	}

	public List<MessageDetail> getMessages(Long conversationId) {
		return messageRepository.findAllByConversationId(conversationId)
				.stream().map(MessageDetail::from).toList();
	}
}
