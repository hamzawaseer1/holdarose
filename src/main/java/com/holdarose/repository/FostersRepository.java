package com.holdarose.repository;

import com.holdarose.domain.Fosters;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Fosters entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FostersRepository extends MongoRepository<Fosters, String> {}
