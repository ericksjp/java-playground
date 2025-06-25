package com.app.park_api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.park_api.entity.User;
import com.app.park_api.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Transactional
    public User save(User user) {
        return repository.save(user);
    }

    @Transactional(readOnly = true)
    public User findById(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new RuntimeException("User not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return repository.findAll();
    }

    @Transactional
    public User udpatePassword(Long id, String currentPassword, String newPassword, String confirmNewPassword) {
        if (!newPassword.equals(confirmNewPassword)) {
            throw new RuntimeException("New password and confirmation do not match");
        }

        User user = findById(id);

        if (!user.getPassword().equals(currentPassword)) {
            throw new RuntimeException("Current password is incorrect");
        }

        user.setPassword(newPassword);
        return user;
    }
}
