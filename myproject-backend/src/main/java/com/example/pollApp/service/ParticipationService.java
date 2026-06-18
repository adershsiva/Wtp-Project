package com.example.pollApp.service;

import com.example.pollApp.dto.ParticipationDTO;
import com.example.pollApp.entity.*;
import com.example.pollApp.exception.ResourceNotFoundException;
import com.example.pollApp.mapper.ParticipationMapper;
import com.example.pollApp.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParticipationService {

    private final ParticipationRepository participationRepository;
    private final PollRepository pollRepository;
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final ParticipationMapper participationMapper;

    @Transactional
    public ParticipationDTO participate(ParticipationDTO participationDTO, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        Poll poll = pollRepository.findById(participationDTO.getPollId())
                .orElseThrow(() -> new ResourceNotFoundException("Poll not found"));

        if (poll.isFinished()) {
            throw new IllegalStateException("Cannot participate in a finished poll");
        }

        if (poll.getDueDate().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Cannot participate in a poll after its due date");
        }

        if (!poll.getInvitedUsers().contains(user)) {
            throw new IllegalStateException("User is not invited to this poll");
        }

        // Check if already participated
        if (participationRepository.findByPollAndUser(poll, user).isPresent()) {
            throw new IllegalStateException("User already participated in this poll");
        }

        if (participationDTO.getAnswers().size() != poll.getQuestions().size()) {
            throw new IllegalStateException("All questions must be answered");
        }

        Participation participation = new Participation();
        participation.setPoll(poll);
        participation.setUser(user);

        List<Answer> answers = participationDTO.getAnswers().stream().map(aDto -> {
            Question question = questionRepository.findById(aDto.getQuestionId())
                    .orElseThrow(() -> new ResourceNotFoundException("Question not found: " + aDto.getQuestionId()));
            
            // Validation for numeric type
            if (question.getType() == QuestionType.NUMERIC_1_TO_5) {
                try {
                    int val = Integer.parseInt(aDto.getContent());
                    if (val < 1 || val > 5) throw new Exception();
                } catch (Exception e) {
                    throw new IllegalStateException("Numeric answers must be between 1 and 5");
                }
            }

            Answer answer = new Answer();
            answer.setQuestion(question);
            answer.setContent(aDto.getContent());
            answer.setParticipation(participation);
            return answer;
        }).collect(Collectors.toList());

        participation.setAnswers(answers);
        
        return participationMapper.toDto(participationRepository.save(participation));
    }

    @Transactional(readOnly = true)
    public List<Poll> getPendingPolls(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        LocalDateTime now = LocalDateTime.now();

        // Get polls where user is invited
        // Filtering in memory for simplicity in this prototype, 
        // could be optimized with a custom repository query.
        return pollRepository.findAll(Sort.by(Sort.Direction.DESC, "dueDate")).stream()
                .filter(p -> p.getInvitedUsers().contains(user))
                .filter(p -> !p.isFinished())
                .filter(p -> p.getDueDate().isAfter(now))
                .filter(p -> participationRepository.findByPollAndUser(p, user).isEmpty())
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ParticipationDTO getParticipation(Long id) {
        return participationRepository.findById(id)
                .map(participationMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Participation not found"));
    }

    @Transactional(readOnly = true)
    public List<ParticipationDTO> getUserParticipations(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return participationRepository.findByUser(user).stream()
                .map(participationMapper::toDto)
                .collect(Collectors.toList());
    }
}
