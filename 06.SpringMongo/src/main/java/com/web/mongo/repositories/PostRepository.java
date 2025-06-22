package com.web.mongo.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.web.mongo.domain.Post;

public interface PostRepository extends MongoRepository<Post, String> {}
