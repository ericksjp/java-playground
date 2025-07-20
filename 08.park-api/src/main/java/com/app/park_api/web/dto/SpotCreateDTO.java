package com.app.park_api.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class SpotCreateDTO {
    @NotBlank(message = "{NotBlank.spotCreateDTO.code}")
    @Size(max = 4, min = 4, message = "{Size.spotCreateDTO.code}")
    private String code;

    @NotBlank(message = "{NotBlank.spotCreateDTO.status}")
    @Pattern(regexp = "(?i)FREE|OCCUPIED", message = "{Pattern.spotCreateDTO.status}")
    private String status;
}
