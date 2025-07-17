package com.playground.mscards.web.mapper;

import java.util.List;

import org.modelmapper.ModelMapper;

import com.playground.mscards.entity.ClientCard;
import com.playground.mscards.web.dto.ClientCardResponseDto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientCardMapper {
    public static ClientCardResponseDto toDto(ClientCard entity) {
        return new ModelMapper().map(entity, ClientCardResponseDto.class);
    }

    public static List<ClientCardResponseDto> toDtoList(List<ClientCard> dtos) {
        return dtos.stream()
                   .map(ClientCardMapper::toDto)
                   .toList();
    }
}

