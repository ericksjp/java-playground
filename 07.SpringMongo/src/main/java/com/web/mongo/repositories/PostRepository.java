package com.web.mongo.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.web.mongo.domain.Post;

@Repository
public interface PostRepository extends MongoRepository<Post, String> {}
