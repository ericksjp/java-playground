package com.web.mongo.resources;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.web.mongo.domain.Post;
import com.web.mongo.resources.util.URL;
import com.web.mongo.services.PostService;

@RestController
@RequestMapping(value = "/posts")
public class PostResource {

    @Autowired
    private PostService postService;

    @GetMapping
    public ResponseEntity<List<Post>> findAll(
        @RequestParam (required = false, name = "text") String text,
        @RequestParam (required = false, name = "minDate") String minDate,
        @RequestParam (required = false, name = "maxDate") String maxDate,
        @RequestParam (required = false, name = "page", defaultValue = "0") Integer page,
        @RequestParam (required = false, name = "size", defaultValue = "20") Integer PageSize
    ) {
        text = URL.decodeURL(text);
        minDate = URL.decodeURL(minDate);
        maxDate = URL.decodeURL(maxDate);

        LocalDate parsedMinDate = URL.decodeLocalDate(minDate, null);
        LocalDate parsedMaxDate = URL.decodeLocalDate(maxDate, null);

        parsedMaxDate = parsedMaxDate == null ? null : parsedMaxDate.plusDays(1);

        List<Post> posts = postService.findAll(text, parsedMinDate, parsedMaxDate, page, PageSize);

        return ResponseEntity.ok().body(posts);
    }
}
