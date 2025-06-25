package com.app.park_api.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class UserPasswordDTO {
    @NotBlank
    @Size(min = 6, max = 6, message = "password must be 6 characters long")
    private String currentPassword;
    @NotBlank
    @Size(min = 6, max = 6, message = "password must be 6 characters long")
    private String newPassword;
    @NotBlank
    @Size(min = 6, max = 6, message = "password must be 6 characters long")
    private String confirmNewPassword;
}
