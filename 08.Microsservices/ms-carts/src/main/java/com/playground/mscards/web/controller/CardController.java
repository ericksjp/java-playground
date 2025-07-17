package com.playground.mscards.web.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.playground.mscards.entity.Card;
import com.playground.mscards.service.CardService;
import com.playground.mscards.web.dto.CreateCardDto;
import com.playground.mscards.web.dto.ResponseCardDto;
import com.playground.mscards.web.mapper.CardMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/cards")
public class CardController {
    private final CardService cardService;

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody CreateCardDto dto) {
        Card created = cardService.save(CardMapper.toEntity(dto));

        URI locationHeader = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .query("cpf={cpf}")
            .buildAndExpand(created.getId())
            .toUri();


        return ResponseEntity.created(locationHeader).build();
    }

    public ResponseEntity<List<ResponseCardDto>> findByIncomeGreatherThan(
        @RequestParam(required = true) Double value
    ) {
        List<Card> cards = cardService.getByIncomeGreatherThane(value);

        return ResponseEntity.ok(CardMapper.toDtoList(cards));
    }
    
}
