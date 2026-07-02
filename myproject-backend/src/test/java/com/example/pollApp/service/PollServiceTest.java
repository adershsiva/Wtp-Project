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

import java.time.LocalDateTime;
import java.util.List;
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

    @Test
    void shouldUpdatePoll() {
        // Given
        Long pollId = 1L;
        Long creatorId = 2L;
        User creator = User.builder().id(creatorId).username("creator").build();
        Poll existingPoll = Poll.builder().id(pollId).title("Old Title").creator(creator).build();
        PollDTO updateDto = PollDTO.builder().title("New Title").description("New Desc").dueDate(LocalDateTime.now().plusDays(1)).build();
        Poll updatedPoll = Poll.builder().id(pollId).title("New Title").description("New Desc").creator(creator).build();
        PollDTO expectedDto = PollDTO.builder().id(pollId).title("New Title").description("New Desc").build();

        when(pollRepository.findById(pollId)).thenReturn(Optional.of(existingPoll));
        when(pollRepository.existsByTitleAndIdNot("New Title", pollId)).thenReturn(false);
        when(pollRepository.save(any())).thenReturn(updatedPoll);
        when(pollMapper.toDto(any())).thenReturn(expectedDto);

        // When
        PollDTO result = pollService.updatePoll(pollId, updateDto, creatorId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("New Title");
    }

    @Test
    void shouldThrowExceptionWhenNonCreatorTriesToUpdatePoll() {
        // Given
        Long pollId = 1L;
        Long creatorId = 2L;
        Long nonCreatorId = 3L;
        User creator = User.builder().id(creatorId).username("creator").build();
        Poll existingPoll = Poll.builder().id(pollId).title("Old Title").creator(creator).build();
        PollDTO updateDto = PollDTO.builder().title("New Title").build();

        when(pollRepository.findById(pollId)).thenReturn(Optional.of(existingPoll));

        // When & Then
        org.junit.jupiter.api.Assertions.assertThrows(IllegalStateException.class, () -> {
            pollService.updatePoll(pollId, updateDto, nonCreatorId);
        });
    }

    @Test
    void shouldThrowExceptionWhenUpdatingToDuplicateTitle() {
        // Given
        Long pollId = 1L;
        Long creatorId = 2L;
        User creator = User.builder().id(creatorId).username("creator").build();
        Poll existingPoll = Poll.builder().id(pollId).title("Old Title").creator(creator).build();
        PollDTO updateDto = PollDTO.builder().title("Duplicate Title").build();

        when(pollRepository.findById(pollId)).thenReturn(Optional.of(existingPoll));
        when(pollRepository.existsByTitleAndIdNot("Duplicate Title", pollId)).thenReturn(true);

        // When & Then
        org.junit.jupiter.api.Assertions.assertThrows(IllegalStateException.class, () -> {
            pollService.updatePoll(pollId, updateDto, creatorId);
        });
    }

    @Test
    void shouldReturnVisiblePolls() {
        // Given
        Long userId = 1L;
        Poll poll1 = Poll.builder().id(101L).title("Poll 1").build();
        Poll poll2 = Poll.builder().id(102L).title("Poll 2").build();
        List<Poll> visiblePolls = List.of(poll1, poll2);

        PollDTO dto1 = PollDTO.builder().title("Poll 1").build();
        PollDTO dto2 = PollDTO.builder().title("Poll 2").build();

        when(pollRepository.findVisiblePolls(userId)).thenReturn(visiblePolls);
        when(pollMapper.toDto(poll1)).thenReturn(dto1);
        when(pollMapper.toDto(poll2)).thenReturn(dto2);

        // When
        List<PollDTO> result = pollService.getAllPolls(userId);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getTitle()).isEqualTo("Poll 1");
        assertThat(result.get(1).getTitle()).isEqualTo("Poll 2");
    }

    @Test
    void shouldReturnEmptyListWhenUserIdIsNullForVisiblePolls() {
        // When
        List<PollDTO> result = pollService.getAllPolls(null);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void shouldDeletePoll() {
        // Given
        Long pollId = 1L;
        Long creatorId = 2L;
        User creator = User.builder().id(creatorId).username("creator").build();
        Poll poll = Poll.builder().id(pollId).creator(creator).build();

        when(pollRepository.findById(pollId)).thenReturn(Optional.of(poll));

        // When
        pollService.deletePoll(pollId, creatorId);

        // Then
        org.mockito.Mockito.verify(pollRepository).delete(poll);
    }

    @Test
    void shouldThrowExceptionWhenNonCreatorTriesToDeletePoll() {
        // Given
        Long pollId = 1L;
        Long creatorId = 2L;
        Long nonCreatorId = 3L;
        User creator = User.builder().id(creatorId).username("creator").build();
        Poll poll = Poll.builder().id(pollId).creator(creator).build();

        when(pollRepository.findById(pollId)).thenReturn(Optional.of(poll));

        // When & Then
        org.junit.jupiter.api.Assertions.assertThrows(IllegalStateException.class, () -> {
            pollService.deletePoll(pollId, nonCreatorId);
        });
    }

    @Test
    void shouldPausePoll() {
        // Given
        Long pollId = 1L;
        Long creatorId = 2L;
        User creator = User.builder().id(creatorId).username("creator").build();
        Poll poll = Poll.builder().id(pollId).creator(creator).finished(false).paused(false).build();
        PollDTO expectedDto = PollDTO.builder().id(pollId).paused(true).build();

        when(pollRepository.findById(pollId)).thenReturn(Optional.of(poll));
        when(pollRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(pollMapper.toDto(any())).thenReturn(expectedDto);

        // When
        PollDTO result = pollService.pausePoll(pollId, creatorId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.isPaused()).isTrue();
        assertThat(poll.isPaused()).isTrue();
    }

    @Test
    void shouldResumePoll() {
        // Given
        Long pollId = 1L;
        Long creatorId = 2L;
        User creator = User.builder().id(creatorId).username("creator").build();
        Poll poll = Poll.builder().id(pollId).creator(creator).finished(false).paused(true).build();
        PollDTO expectedDto = PollDTO.builder().id(pollId).paused(false).build();

        when(pollRepository.findById(pollId)).thenReturn(Optional.of(poll));
        when(pollRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(pollMapper.toDto(any())).thenReturn(expectedDto);

        // When
        PollDTO result = pollService.resumePoll(pollId, creatorId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.isPaused()).isFalse();
        assertThat(poll.isPaused()).isFalse();
    }

    @Test
    void shouldThrowExceptionWhenNonCreatorTriesToPausePoll() {
        // Given
        Long pollId = 1L;
        Long creatorId = 2L;
        Long nonCreatorId = 3L;
        User creator = User.builder().id(creatorId).username("creator").build();
        Poll poll = Poll.builder().id(pollId).creator(creator).finished(false).build();

        when(pollRepository.findById(pollId)).thenReturn(Optional.of(poll));

        // When & Then
        org.junit.jupiter.api.Assertions.assertThrows(IllegalStateException.class, () -> {
            pollService.pausePoll(pollId, nonCreatorId);
        });
    }

    @Test
    void shouldThrowExceptionWhenTriesToPauseFinishedPoll() {
        // Given
        Long pollId = 1L;
        Long creatorId = 2L;
        User creator = User.builder().id(creatorId).username("creator").build();
        Poll poll = Poll.builder().id(pollId).creator(creator).finished(true).build();

        when(pollRepository.findById(pollId)).thenReturn(Optional.of(poll));

        // When & Then
        org.junit.jupiter.api.Assertions.assertThrows(IllegalStateException.class, () -> {
            pollService.pausePoll(pollId, creatorId);
        });
    }
}
