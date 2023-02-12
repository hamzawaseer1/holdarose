package com.holdarose.repository;

import com.holdarose.domain.Donation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Donation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DonationRepository extends MongoRepository<Donation, String> {}
