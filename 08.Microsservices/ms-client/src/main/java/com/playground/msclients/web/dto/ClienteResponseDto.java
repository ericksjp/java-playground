package com.playground.msclients.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class ClienteResponseDto {
    private Long id;
    private String name;
    private String cpf;
    private Integer age;
}
