package com.app.park_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.park_api.entity.Spot;
import com.app.park_api.entity.Spot.SpotStatus;

public interface SpotRepository extends JpaRepository<Spot, Long> {
    Optional<Spot> findByCode(String code);
    Optional<Spot> findFirstByStatus(SpotStatus status);
}
