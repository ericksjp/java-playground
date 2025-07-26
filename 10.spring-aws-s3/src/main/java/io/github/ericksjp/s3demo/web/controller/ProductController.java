package io.github.ericksjp.s3demo.web.controller;

import java.io.IOException;
import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.amazonaws.AmazonClientException;

import io.github.ericksjp.s3demo.models.InputStreamObject;
import io.github.ericksjp.s3demo.service.S3Service;
import io.github.ericksjp.s3demo.web.dto.ProductListDto;
import io.github.ericksjp.s3demo.web.mapper.ProductListMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final S3Service awsService;

    @PutMapping("/{store_name}")
    public ResponseEntity<Void> upsertProductList(
        @PathVariable String store_name, 
        @RequestBody @Valid ProductListDto dto
    ) throws IOException {
        InputStreamObject streamObj = ProductListMapper.toInputStream(dto);
        awsService.upload(store_name + ".json", "json", streamObj);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/products/{store_name}")
                .buildAndExpand(store_name).toUri();

        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/{store_name}")
    public ResponseEntity<ProductListDto> getProductList(
        @PathVariable String store_name
    ) throws AmazonClientException, IOException {
        byte[] object = awsService.download(store_name + ".json");
        ProductListDto dto = ProductListMapper.toDto(object);

        return ResponseEntity.ok(dto);
    }
}
