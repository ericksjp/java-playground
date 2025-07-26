package io.github.ericksjp.s3demo.web.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProductDto {
    @NotBlank
    private String name;

    @NotNull
    @Positive(message = "Description must be a positive number")
    private BigDecimal price;

    @NotNull
    @Size(min = 0, max = 100, message = "discount_percent must be between 0 and 100")
    private String discount_percent;
}
