package com.app.park_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.park_api.entity.Spot;

public interface SpotRepository extends JpaRepository<Spot, Long> {
    Optional<Spot> findByCode(String code);
}
