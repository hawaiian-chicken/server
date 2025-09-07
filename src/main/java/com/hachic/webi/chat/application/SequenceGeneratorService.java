package com.hachic.webi.chat.application;

import java.util.Objects;

import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.hachic.webi.chat.domain.AutoIncrementSequence;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SequenceGeneratorService {

	private final MongoOperations mongoOperations;

	/**
	 * 주어진 seqName의 값을 원자적으로 1 증가시키고 증가된 값을 반환함
	 * 없을 경우 생성 후 1 반환함
	 * @param seqName 시퀀스 식별자(_id)
	 * @return 증가된 시퀀스 값
	 */
	public long generateSequence(String seqName) {
		AutoIncrementSequence counter = mongoOperations.findAndModify(Query.query(
				Criteria.where("_id").is(seqName)),
				new Update().inc("seq", 1),
				FindAndModifyOptions.options().returnNew(true).upsert(true),
				AutoIncrementSequence.class);

		return !Objects.isNull(counter) ? counter.getSeq() : 1;
	}
}
