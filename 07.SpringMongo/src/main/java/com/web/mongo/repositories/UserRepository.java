package com.web.mongo.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.web.mongo.domain.User;

public interface UserRepository extends MongoRepository<User, String> {}
