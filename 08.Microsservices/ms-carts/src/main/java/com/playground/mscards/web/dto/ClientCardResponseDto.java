package com.playground.mscards.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ClientCardResponseDto {
    private String name;
    private String brand;
    private Double creditLimit;
}
