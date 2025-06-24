package com.app.park_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.park_api.entity.User;
import com.app.park_api.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Transactional
    public User save(User user) {
        return repository.save(user);
    }

    public User findById(Long id) {
        return repository.findById(id).orElseThrow(
            () -> new RuntimeException("User not found with id: " + id)
        );
    }
}
