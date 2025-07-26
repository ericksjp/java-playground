package com.playground.mscards.web.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.playground.mscards.entity.ClientCard;
import com.playground.mscards.service.ClientCartService;
import com.playground.mscards.web.dto.ClientCardResponseDto;
import com.playground.mscards.web.mapper.ClientCardMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/cards/client")
public class ClientCardController {
    private final ClientCartService clientCartService;

    @GetMapping(params = "cpf")
    public ResponseEntity<List<ClientCardResponseDto>> getCardsByClient(
            @RequestParam("cpf") String cpf) {
        List<ClientCard> cards = clientCartService.getCardsByCpf(cpf);
        List<ClientCardResponseDto> responseDtos = ClientCardMapper.toDtoList(cards);

        return ResponseEntity.ok(ClientCardMapper.toDtoList(cards));
    }

}
