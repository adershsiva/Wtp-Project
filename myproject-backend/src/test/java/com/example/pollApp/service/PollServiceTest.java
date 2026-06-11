package com.example.pollApp.service;

import com.example.pollApp.dto.PollDTO;
import com.example.pollApp.entity.Poll;
import com.example.pollApp.entity.User;
import com.example.pollApp.mapper.PollMapper;
import com.example.pollApp.repository.PollRepository;
import com.example.pollApp.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PollServiceTest {

    @Mock
    private PollRepository pollRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PollMapper pollMapper;

    @InjectMocks
    private PollService pollService;

    @Test
    void shouldCreatePoll() {
        // Given
        Long userId = 1L;
        User user = User.builder().id(userId).username("test").build();
        PollDTO pollDTO = PollDTO.builder().title("Title").build();
        Poll poll = Poll.builder().title("Title").build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(pollMapper.toEntity(any())).thenReturn(poll);
        when(pollRepository.save(any())).thenReturn(poll);
        when(pollMapper.toDto(any())).thenReturn(pollDTO);

        // When
        PollDTO result = pollService.createPoll(pollDTO, userId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Title");
    }
}
