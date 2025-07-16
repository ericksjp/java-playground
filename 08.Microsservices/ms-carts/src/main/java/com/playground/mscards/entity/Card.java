package com.playground.mscards.entity;

import org.springframework.data.annotation.Id;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Entity(name = "carts")
@Data
public class Card {
    @Id
    private Long id;
    private String name;
    @Enumerated(EnumType.STRING)
    private CardBrant brand;
    private Double income;
    private Double limit;
}
