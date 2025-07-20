package com.playground.mscards.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CardIssueDto {
    private Long idCard;
    private String cpf;
    private Double creditLimit;
}
