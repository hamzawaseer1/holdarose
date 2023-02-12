package com.holdarose.repository;

import com.holdarose.domain.AdoptionRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data MongoDB repository for the AdoptionRequest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AdoptionRequestRepository extends MongoRepository<AdoptionRequest, String> {
    List<AdoptionRequest> findAdoptionRequestByApprovedFalseAndFoundationName(String name);
}
