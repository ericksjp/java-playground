package io.github.ericksjp.s3demo.web.mapper;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.ericksjp.s3demo.models.InputStreamObject;
import io.github.ericksjp.s3demo.web.dto.ProductListDto;

public class ProductListMapper {

    public static ProductListDto toDto(byte[] data) throws IOException {
        return new ObjectMapper().readValue(data, ProductListDto.class);
    }

    public static InputStreamObject toInputStream(ProductListDto productListDto) throws IOException {
        byte[] jsonBytes = new ObjectMapper().writeValueAsBytes(productListDto);
        var result = new InputStreamObject(new ByteArrayInputStream(jsonBytes), Long.valueOf(jsonBytes.length));
        return result;
    }
}
