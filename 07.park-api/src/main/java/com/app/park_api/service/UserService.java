package com.app.park_api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.park_api.entity.User;
import com.app.park_api.exception.InvalidPasswordException;
import com.app.park_api.exception.ResourceNotFoundException;
import com.app.park_api.exception.UsernameUniqueViolationException;
import com.app.park_api.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public User save(User user) {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return repository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new UsernameUniqueViolationException(String.format("username '%s' already in use", user.getUsername()));
        }
    }

    @Transactional(readOnly = true)
    public User findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format("user with id '%d' not found", id)));
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return repository.findAll();
    }

    @Transactional
    public User udpatePassword(Long id, String currentPassword, String newPassword, String confirmNewPassword) {
        if (!newPassword.equals(confirmNewPassword)) {
            throw new InvalidPasswordException("new password and confirmation do not match");
        }

        User user = findById(id);

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new InvalidPasswordException("current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        return user;
    }

    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        return repository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException(String.format("user with username '%s' not found", username)));
    }
}
