package com.app.park_api.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.park_api.entity.Spot;
import com.app.park_api.entity.Spot.SpotStatus;
import com.app.park_api.exception.ResourceNotFoundException;
import com.app.park_api.exception.ResourceUniqueViolationException;
import com.app.park_api.repository.SpotRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SpotService {
    private final SpotRepository repository;

    @Transactional
    public Spot save(Spot Spot) {
        try {
            return repository.save(Spot);
        } catch (DataIntegrityViolationException e) {
            throw new ResourceUniqueViolationException("spot", "code", Spot.getCode());
        }
    }

    @Transactional(readOnly = true)
    public Spot findByCode(String code) {
        return repository.findByCode(code).orElseThrow(() -> new ResourceNotFoundException("spot", "code", code.toString()));
    }

    @Transactional(readOnly = true)
    public Spot findFreeSpot() {
        return repository.findFirstByStatus(SpotStatus.FREE).orElseThrow(() -> new ResourceNotFoundException("spot", "status", SpotStatus.FREE.toString()));
    }
}
