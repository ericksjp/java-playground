package com.playground.mscreditAnalizer.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.playground.mscreditAnalizer.web.dto.CardDto;
import com.playground.mscreditAnalizer.web.dto.ClientCardDto;

@FeignClient(value = "mscards", path = "/cards")
public interface CardResourceClient {

    @GetMapping(params = "cpf")
    ResponseEntity<List<ClientCardDto>> findByCpf(@PathVariable String cpf);

    @GetMapping(params = "income")
    ResponseEntity<List<CardDto>> findByIncomeGreatherThan(@RequestParam Double income);
    
}
