package io.github.ericksjp.s3demo.web.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ProductListDto {
    @NotNull
    @Size(min = 1, max = 10, message = "List of products must contain between 1 and 10 items")
    @Valid
    private List<ProductDto> products;
}
