package com.app.park_api.web.dto.mapper;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import com.app.park_api.entity.User;
import com.app.park_api.web.dto.UserCreateDTO;
import com.app.park_api.web.dto.UserResponseDTO;

public class UserMapper {
    public static User toUser(UserCreateDTO user) {
        return new ModelMapper().map(user, User.class);
    }

    public static UserResponseDTO toDTO(User user) {
        String role = user.getRole().name().substring("ROLE_".length());

        PropertyMap<User, UserResponseDTO> props = new PropertyMap<User, UserResponseDTO>() {
            @Override
            protected void configure() {
                map().setRole(role);
            }
        };

        ModelMapper mapper = new ModelMapper();
        mapper.addMappings(props);

        return mapper.map(user, UserResponseDTO.class);
    }

    public static List<UserResponseDTO> toListDTO(List<User> list) {
        return list.stream().map(UserMapper::toDTO).toList();
    }
}
