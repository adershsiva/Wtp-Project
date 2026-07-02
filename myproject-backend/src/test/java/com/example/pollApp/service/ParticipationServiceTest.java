package com.example.pollApp.service;

import com.example.pollApp.entity.Participation;
import com.example.pollApp.entity.Poll;
import com.example.pollApp.entity.Question;
import com.example.pollApp.entity.User;
import com.example.pollApp.repository.ParticipationRepository;
import com.example.pollApp.repository.PollRepository;
import com.example.pollApp.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ParticipationServiceTest {

    @Mock
    private ParticipationRepository participationRepository;

    @Mock
    private PollRepository pollRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ParticipationService participationService;

    @Test
    void shouldGetPendingPollsOrderedByDueDateDescAndExcludeAnswered() {
        // Given
        Long userId = 1L;
        User user = User.builder().id(userId).username("testuser").build();

        // 3 Polls:
        // Poll 1: due tomorrow, invited, not answered
        Poll poll1 = Poll.builder()
                .id(101L)
                .title("Poll 1")
                .dueDate(LocalDateTime.now().plusDays(1))
                .finished(false)
                .invitedUsers(new ArrayList<>(List.of(user)))
                .questions(new ArrayList<>(List.of(new Question())))
                .build();

        // Poll 2: due in 2 days, invited, not answered
        Poll poll2 = Poll.builder()
                .id(102L)
                .title("Poll 2")
                .dueDate(LocalDateTime.now().plusDays(2))
                .finished(false)
                .invitedUsers(new ArrayList<>(List.of(user)))
                .questions(new ArrayList<>(List.of(new Question(), new Question())))
                .build();

        // Poll 3: due in 3 days, invited, but ALREADY answered
        Poll poll3 = Poll.builder()
                .id(103L)
                .title("Poll 3")
                .dueDate(LocalDateTime.now().plusDays(3))
                .finished(false)
                .invitedUsers(new ArrayList<>(List.of(user)))
                .questions(new ArrayList<>(List.of(new Question())))
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        // Mock pollRepository to return all polls sorted descending by due date
        // Descending order: Poll 3 (3 days), Poll 2 (2 days), Poll 1 (1 day)
        when(pollRepository.findAll(any(Sort.class))).thenReturn(List.of(poll3, poll2, poll1));

        // Mock participation check: Poll 3 is answered, others are empty
        when(participationRepository.findByPollAndUser(poll3, user)).thenReturn(Optional.of(new Participation()));
        when(participationRepository.findByPollAndUser(poll2, user)).thenReturn(Optional.empty());
        when(participationRepository.findByPollAndUser(poll1, user)).thenReturn(Optional.empty());

        // When
        List<Poll> pendingPolls = participationService.getPendingPolls(userId);

        // Then
        // Should exclude Poll 3 (already answered)
        // Should order by descending due date: Poll 2 (2 days) then Poll 1 (1 day)
        assertThat(pendingPolls).hasSize(2);
        assertThat(pendingPolls.get(0).getTitle()).isEqualTo("Poll 2");
        assertThat(pendingPolls.get(0).getQuestions()).hasSize(2);
        assertThat(pendingPolls.get(1).getTitle()).isEqualTo("Poll 1");
        assertThat(pendingPolls.get(1).getQuestions()).hasSize(1);
    }
}
