package com.example.pollApp.service;

import com.example.pollApp.dto.PollResultDTO;
import com.example.pollApp.dto.ParticipationDTO;
import com.example.pollApp.entity.Poll;
import com.example.pollApp.entity.User;
import com.example.pollApp.repository.PollRepository;
import com.example.pollApp.repository.ParticipationRepository;
import com.example.pollApp.mapper.ParticipationMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ResultServiceTest {

    @Mock
    private PollRepository pollRepository;

    @Mock
    private ParticipationRepository participationRepository;

    @Mock
    private ParticipationMapper participationMapper;

    @InjectMocks
    private ResultService resultService;

    @Test
    void shouldGetAggregateResultsBeforeFinished() {
        // Given
        Long pollId = 1L;
        Long creatorId = 2L;
        User creator = User.builder().id(creatorId).username("creator").build();
        Poll poll = Poll.builder().id(pollId).title("Test Poll").creator(creator).finished(false).build();

        when(pollRepository.findById(pollId)).thenReturn(Optional.of(poll));
        when(participationRepository.findByPoll(poll)).thenReturn(Collections.emptyList());

        // When
        PollResultDTO result = resultService.getAggregateResults(pollId, creatorId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getPollId()).isEqualTo(pollId);
        assertThat(result.getPollTitle()).isEqualTo("Test Poll");
        assertThat(result.getQuestionResults()).isEmpty();
    }

    @Test
    void shouldGetIndividualResponsesBeforeFinished() {
        // Given
        Long pollId = 1L;
        Long creatorId = 2L;
        User creator = User.builder().id(creatorId).username("creator").build();
        Poll poll = Poll.builder().id(pollId).title("Test Poll").creator(creator).finished(false).build();

        when(pollRepository.findById(pollId)).thenReturn(Optional.of(poll));
        when(participationRepository.findByPoll(poll)).thenReturn(Collections.emptyList());

        // When
        List<ParticipationDTO> result = resultService.getIndividualResponses(pollId, creatorId);

        // Then
        assertThat(result).isEmpty();
    }
}
