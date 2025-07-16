package com.playground.msclients.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ClienteResponseDto {
    private Long id;
    private String name;
    private String cpf;
    private Integer age;
}
