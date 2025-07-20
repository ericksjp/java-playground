package com.playground.mscards.web.dto;

import com.playground.mscards.entity.Card.Brant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ResponseCardDto {
    private Long id;
    private String name;
    private Brant brand;
    private Double income;
    private Double creditLimit;
}

