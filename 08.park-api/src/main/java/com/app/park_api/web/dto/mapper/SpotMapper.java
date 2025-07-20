package com.app.park_api.web.dto.mapper;

import org.modelmapper.ModelMapper;

import com.app.park_api.entity.Spot;
import com.app.park_api.web.dto.SpotCreateDTO;
import com.app.park_api.web.dto.SpotResponseDTO;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SpotMapper {

    public static Spot toSpot(SpotCreateDTO spotCreateDTO) {
        return new ModelMapper().map(spotCreateDTO, Spot.class);
    }

    public static SpotResponseDTO toDTO(Spot spot) {
        return new ModelMapper().map(spot, SpotResponseDTO.class);
    }
}
