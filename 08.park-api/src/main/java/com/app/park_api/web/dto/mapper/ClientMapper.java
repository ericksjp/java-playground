package com.app.park_api.web.dto.mapper;

import java.util.List;

import org.modelmapper.ModelMapper;

import com.app.park_api.entity.Client;
import com.app.park_api.web.dto.ClientCreateDTO;
import com.app.park_api.web.dto.ClientResponseDTO;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientMapper {

    public static Client toClient(ClientCreateDTO dto) {
        return new ModelMapper().map(dto, Client.class);
    }

    public static ClientResponseDTO toDTO(Client client) {
        return new ModelMapper().map(client, ClientResponseDTO.class);
     }

    public static List<ClientResponseDTO> toListDTO(List<Client> list) {
        return list.stream().map(ClientMapper::toDTO).toList();
    }
}
