package com.app.park_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.park_api.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    
}
