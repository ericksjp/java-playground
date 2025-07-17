package com.playground.mscards.web.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.playground.mscards.entity.Card;
import com.playground.mscards.entity.ClientCard;
import com.playground.mscards.service.CardService;
import com.playground.mscards.service.ClientCartService;
import com.playground.mscards.web.dto.ClientCardResponseDto;
import com.playground.mscards.web.dto.CreateCardDto;
import com.playground.mscards.web.dto.ResponseCardDto;
import com.playground.mscards.web.mapper.CardMapper;
import com.playground.mscards.web.mapper.ClientCardMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/cards")
public class CardController {
    private final CardService cardService;
    private final ClientCartService clientCartService;

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody CreateCardDto dto) {
        Card created = cardService.save(CardMapper.toEntity(dto));
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/cards/{id}").buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @GetMapping(params = "income")
    public ResponseEntity<List<ResponseCardDto>> findByIncomeGreatherThan(
        @RequestParam(required = true) Double income
    ) {
        List<Card> cards = cardService.getByIncomeGreatherThane(income);

        return ResponseEntity.ok(CardMapper.toDtoList(cards));
    }

    @GetMapping(params = "cpf")
    public ResponseEntity<List<ClientCardResponseDto>> getCartoesByCliente(
            @RequestParam("cpf") String cpf){
        List<ClientCard> cards = clientCartService.getCardsByCpf(cpf);
        return ResponseEntity.ok(ClientCardMapper.toDtoList(cards));
    }
    
}
