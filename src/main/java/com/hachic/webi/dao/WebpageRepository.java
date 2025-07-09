package com.hachic.webi.dao;

import com.hachic.webi.domain.Webpage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface WebpageRepository extends MongoRepository<Webpage, String> {

    Optional<Webpage> findByUuid(String uuid);
}
