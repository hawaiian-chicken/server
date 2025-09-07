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
	 * AutoIncrementSequence의 seq를 자동으로 증가시키는 메서드
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
