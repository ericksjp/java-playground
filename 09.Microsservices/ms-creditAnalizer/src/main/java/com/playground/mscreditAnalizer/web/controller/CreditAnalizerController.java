package com.playground.mscreditAnalizer.web.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.playground.mscreditAnalizer.domain.ClientStatus;
import com.playground.mscreditAnalizer.service.CreditAnalizerService;
import com.playground.mscreditAnalizer.web.dto.ApprovedCardDto;
import com.playground.mscreditAnalizer.web.dto.CardIssueDto;
import com.playground.mscreditAnalizer.web.dto.CardEmissionProtocolDto;
import com.playground.mscreditAnalizer.web.dto.ClientAvaliationDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/credit-analizer")
public class CreditAnalizerController {
    private final CreditAnalizerService creditAnalizerService;

    @GetMapping("/{cpf}")
    public ResponseEntity<ClientStatus> findByCpf(@PathVariable String cpf) {
        ClientStatus status = creditAnalizerService.getClientStatus(cpf);
        return ResponseEntity.ok(status);
    }

    @PostMapping
    public ResponseEntity<List<ApprovedCardDto>> avaliateClient(@RequestBody ClientAvaliationDto dto) {
        var cards = creditAnalizerService.avaliateClient(dto.getCpf(), dto.getIncome());
        return ResponseEntity.ok(cards);
    }

    @PostMapping("issue-card")
    public ResponseEntity<CardEmissionProtocolDto> issueCard(@RequestBody CardIssueDto dto) {
        CardEmissionProtocolDto protocol = creditAnalizerService.issueCard(dto);
        return ResponseEntity.ok(protocol);
    }
}
