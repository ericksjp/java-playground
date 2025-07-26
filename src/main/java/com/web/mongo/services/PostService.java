package com.web.mongo.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.web.mongo.domain.Post;
import com.web.mongo.repositories.PostRepository;

import jakarta.annotation.Nullable;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private MongoTemplate mongoTemplate;
    
    public List<Post> findAll(@Nullable String text, @Nullable LocalDate minDate, @Nullable LocalDate maxDate, 
                              @Nullable Integer page, @Nullable Integer pageSize) {
        Query q = new Query();

        if (text != null && !text.isEmpty()) {
            Criteria textCriteria = new Criteria().orOperator(
                Criteria.where("title").regex(text, "i"),
                Criteria.where("body").regex(text, "i"),
                Criteria.where("comments.text").regex(text, "i")
            );

            q.addCriteria(textCriteria);
        }

        if (minDate != null) {
            q.addCriteria(Criteria.where("date").gte(minDate));
        }

        if (maxDate != null) {
            q.addCriteria(Criteria.where("date").lte(maxDate));
        }

        if (page == null || page < 0) {
            page = 0;
        }

        if (pageSize == null || pageSize <= 0) {
            pageSize = 20;
        }

        q.with(PageRequest.of(page, pageSize));

        return mongoTemplate.find(q, Post.class);
    }
}
