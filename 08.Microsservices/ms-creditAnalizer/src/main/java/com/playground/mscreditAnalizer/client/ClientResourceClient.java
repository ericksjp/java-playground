package com.playground.mscreditAnalizer.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.playground.mscreditAnalizer.web.dto.ClientDataDto;

@FeignClient(value = "msclients", path = "/clients")
public interface ClientResourceClient {
    @GetMapping(params = "cpf")
    ResponseEntity<ClientDataDto> findByCpf(@RequestParam("cpf") String cpf);
}
