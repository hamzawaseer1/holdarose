package com.holdarose.repository;

import com.holdarose.domain.Child;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Child entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChildRepository extends MongoRepository<Child, String> {}
