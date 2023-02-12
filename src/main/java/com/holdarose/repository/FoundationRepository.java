package com.holdarose.repository;

import com.holdarose.domain.Foundation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Foundation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FoundationRepository extends MongoRepository<Foundation, String> {}
