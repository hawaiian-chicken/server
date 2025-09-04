package com.hachic.webi.auth.dao;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.hachic.webi.auth.domain.User;

public interface UserRepository extends MongoRepository<User, ObjectId> {
	Optional<User> findBySocialId(String socialId);
}
