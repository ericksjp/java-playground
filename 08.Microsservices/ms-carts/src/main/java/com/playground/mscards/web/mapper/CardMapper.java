package com.playground.mscards.web.mapper;

import java.util.List;

import org.modelmapper.ModelMapper;

import com.playground.mscards.entity.Card;
import com.playground.mscards.web.dto.CreateCardDto;
import com.playground.mscards.web.dto.ResponseCardDto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CardMapper {
    public static Card toEntity(CreateCardDto dto) {
        return new ModelMapper().map(dto, Card.class);
    }

    public static ResponseCardDto toDto(Card entity) {
        return new ModelMapper().map(entity, ResponseCardDto.class);
    }

    public static List<ResponseCardDto> toDtoList(List<Card> dtos) {
        return dtos.stream()
                   .map(CardMapper::toDto)
                   .toList();
    }
}
