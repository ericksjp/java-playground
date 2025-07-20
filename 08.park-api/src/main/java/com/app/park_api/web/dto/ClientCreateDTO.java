package com.app.park_api.web.dto;

import org.hibernate.validator.constraints.br.CPF;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class ClientCreateDTO {
    @NotBlank
    @Size(min = 3, max = 200, message = "name must be between 3 and 200 characters long")
    private String name;
    @NotBlank
    @CPF
    private String cpf;
}

