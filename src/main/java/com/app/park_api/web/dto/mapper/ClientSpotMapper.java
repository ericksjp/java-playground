package com.app.park_api.web.dto.mapper;

import java.util.List;

import org.modelmapper.ModelMapper;

import com.app.park_api.entity.ClientSpot;
import com.app.park_api.web.dto.ParkingCreateDTO;
import com.app.park_api.web.dto.ParkingResponseDTO;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientSpotMapper {

    public static ClientSpot toClient(ParkingCreateDTO dto) {
        return new ModelMapper().map(dto, ClientSpot.class);
    }

    public static ParkingResponseDTO toDTO(ClientSpot client) {
        return new ModelMapper().map(client, ParkingResponseDTO.class);
     }

    public static List<ParkingResponseDTO> toListDTO(List<ClientSpot> list) {
        return list.stream().map(ClientSpotMapper::toDTO).toList();
    }
}

