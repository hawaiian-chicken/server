package com.hachic.webi.chat.events;

import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

import com.hachic.webi.chat.application.SequenceGeneratorService;
import com.hachic.webi.chat.domain.Conversation;

import lombok.RequiredArgsConstructor;

/**
 * Conversation 저장 직전에 ID가 비어있으면 시퀀스에서 새 ID를 발급해 설정하는 MongoDB Event Listener
 */
@RequiredArgsConstructor
@Component
public class ConversationListener extends AbstractMongoEventListener<Conversation> {

	private final SequenceGeneratorService sequenceGenerator;

	/**
	 * MongoDB 변환(BeforeConvert) 시 호출됨
	 * ID가 null이면 시퀀스로부터 새 ID를 발급해 Conversation에 설정한다
	 */
	@Override
	public void onBeforeConvert(BeforeConvertEvent<Conversation> event) {
		Conversation conversation = event.getSource();
		// ID가 null인 경우에만 새로운 ID 생성 (새로운 conversation 생성 시)
		if (conversation.getId() == null) {
			conversation.setId(sequenceGenerator.generateSequence(Conversation.SEQUENCE_NAME));
		}
	}
}
