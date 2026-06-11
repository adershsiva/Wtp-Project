package com.example.pollApp.service;

import com.example.pollApp.dto.UserDTO;
import com.example.pollApp.entity.User;
import com.example.pollApp.mapper.UserMapper;
import com.example.pollApp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    public UserDTO register(UserDTO userDTO) {
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new IllegalStateException("Username already taken");
        }
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new IllegalStateException("Email already taken");
        }

        User user = userMapper.toEntity(userDTO);
        // Password hashing will be added in Phase 3
        return userMapper.toDto(userRepository.save(user));
    }
}
