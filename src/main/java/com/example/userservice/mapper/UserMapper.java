package com.example.userservice.mapper;

import com.example.userservice.dto.UserRequestDto;
import com.example.userservice.dto.UserResponseDto;
import com.example.userservice.model.User;

public final class UserMapper {

    private UserMapper() {}

    public static User toEntity(UserRequestDto dto) {
        return new User(
                dto.getName().trim(),
                dto.getEmail().trim().toLowerCase(),
                dto.getAge()
        );
    }

    public static UserResponseDto toResponse(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getAge(),
                user.getCreatedAt()
        );
    }
}