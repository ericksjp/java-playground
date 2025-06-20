package com.web.mongo.resources;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.web.mongo.domain.User;

@RestController
@RequestMapping(value = "/users")
public class UserResource {

    @GetMapping
    public ResponseEntity<List<User>> findAll() {
        List<User> users = new ArrayList<>();
        users.add(new User("1", "John Doe", "john@mail.com"));
        return ResponseEntity.ok().body(users);
    }
}
