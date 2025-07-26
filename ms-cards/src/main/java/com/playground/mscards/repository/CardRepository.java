package com.playground.mscards.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.playground.mscards.entity.Card;

public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findByIncomeGreaterThanEqual(Double value);
}
