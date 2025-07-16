package com.hachic.webi.process.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.hachic.webi.process.domain.ProcessResult;

public interface ProcessResultRepository extends MongoRepository<ProcessResult, String> {
}
