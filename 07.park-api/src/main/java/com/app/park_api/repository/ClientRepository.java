package com.app.park_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.park_api.entity.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {}

