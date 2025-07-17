package com.playground.mscreditAnalizer.web.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.playground.mscreditAnalizer.domain.ClientStatus;
import com.playground.mscreditAnalizer.service.CreditAnalizerService;
import com.playground.mscreditAnalizer.web.dto.ApprovedCardDto;
import com.playground.mscreditAnalizer.web.dto.ClientAvaliationDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/clients")
public class CreditAnalizerController {
    private final CreditAnalizerService creditAnalizerService;

    @GetMapping("/{cpf}")
    public ResponseEntity<ClientStatus> findByCpf(@RequestParam String cpf) {
        ClientStatus status = creditAnalizerService.getClientStatus(cpf);
        return ResponseEntity.ok(status);
    }

    @PostMapping
    public ResponseEntity<List<ApprovedCardDto>> avaliateClient(@RequestBody ClientAvaliationDto dto) {
        var cards = creditAnalizerService.avaliateClient(dto.getCpf(), dto.getIncome());
        return ResponseEntity.ok(cards);
    }

}
