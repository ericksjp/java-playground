package com.app.park_api.web.dto.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import com.app.park_api.web.dto.PageableDTO;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PageableMapper {

    public static PageableDTO toDTO(Page<?> pageable) {
        return new ModelMapper().map(pageable, PageableDTO.class);
    }
    
}
