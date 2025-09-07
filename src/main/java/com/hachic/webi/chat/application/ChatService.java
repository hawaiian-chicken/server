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

	/**
	 * conversations(채팅방)의 Document를 생성하고 id를 반환함
	 * @param userId 현재 로그인된 유저의 ID
	 * @return 생성된 채팅방의 ID
	 */
	public ChatRoom createConversation(String userId) {
		Conversation conversation = new Conversation(userId);
		conversationRepository.save(conversation);
		return ChatRoom.of(conversation.getId());
	}

	/**
	 * 현재 로그인된 유저가 생성한 모든 채팅방을 조회함
	 * @param userId 현재 로그인된 유저의 ID
	 * @return 생성된 모든 conversations의 정보를 각각 dto에 담아서 List로 반환
	 */
	public List<ConversationDetail> getConversations(String userId) {
		return conversationRepository.findAllByUserId(userId)
				.stream().map(ConversationDetail::from).toList();
	}

	/**
	 * 특정 conversation의 상세 메시지를 조회함
	 * @param conversationId 조회할 채팅방의 ID
	 * @return 채팅방 내의 메시지 내역을 dto에 담아서 List로 반환
	 */
	public List<MessageDetail> getMessages(Long conversationId) {
		return messageRepository.findAllByConversationId(conversationId)
				.stream().map(MessageDetail::from).toList();
	}
}
