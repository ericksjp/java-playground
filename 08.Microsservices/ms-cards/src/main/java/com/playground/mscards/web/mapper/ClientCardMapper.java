package com.playground.mscards.web.mapper;

import java.util.List;

import com.playground.mscards.entity.ClientCard;
import com.playground.mscards.web.dto.ClientCardResponseDto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientCardMapper {
    public static ClientCardResponseDto toDto(ClientCard entity) {
        return new ClientCardResponseDto(
            entity.getCard().getName(),
            entity.getCard().getBrand().toString(),
            entity.getCreditLimit()
        );
    }

    public static List<ClientCardResponseDto> toDtoList(List<ClientCard> dtos) {
        return dtos.stream()
                   .map(ClientCardMapper::toDto)
                   .toList();
    }
}

