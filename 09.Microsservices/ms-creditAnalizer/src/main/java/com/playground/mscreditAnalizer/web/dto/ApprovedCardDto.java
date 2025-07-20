package com.playground.mscreditAnalizer.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ApprovedCardDto {
    private String card;
    private String brand;
    private Double creditLimit;
}
