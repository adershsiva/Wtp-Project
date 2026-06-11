package com.example.pollApp.mapper;

import com.example.pollApp.dto.UserDTO;
import com.example.pollApp.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDTO toDto(User user) {
        if (user == null) return null;
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                // Password should generally not be mapped back to DTO for security,
                // but for registration DTO it might be present in the incoming DTO.
                .build();
    }

    public User toEntity(UserDTO dto) {
        if (dto == null) return null;
        return User.builder()
                .id(dto.getId())
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .build();
    }
}
