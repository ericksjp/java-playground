package com.playground.msclients.web.mappper;

import org.modelmapper.ModelMapper;

import com.playground.msclients.domain.Client;
import com.playground.msclients.web.dto.ClientCreateDto;
import com.playground.msclients.web.dto.ClienteResponseDto;

public class ClientMapper {
    public static ClienteResponseDto toDto(Client client) {
        return new ModelMapper().map(client, ClienteResponseDto.class);
    }

    public static Client toEntity(ClientCreateDto dto) {
        return new ModelMapper().map(dto, Client.class);
    }
}
