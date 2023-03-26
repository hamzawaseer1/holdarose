package com.holdarose.repository;

import com.holdarose.domain.Child;
import com.holdarose.domain.Foundation;
import com.holdarose.domain.enumeration.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Child entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChildRepository extends MongoRepository<Child, String> {
    Page<Child> findChildByFoundation(Foundation foundation, Pageable pageable);

    Page<Child> findChildByFoundationAndStatus(Foundation foundation, Status status, Pageable pageable);
}
