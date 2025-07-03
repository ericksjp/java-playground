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
    @NotBlank
    @Size(max = 4, min = 4, message = "Code must be exactly 4 characters long")
    private String code;

    @NotBlank
    @Pattern(regexp = "(?i)FREE|OCCUPIED", message = "Status must be either 'FREE' or 'OCCUPIED'")
    private String status;
}
