package com.example.pollApp.service;

import com.example.pollApp.dto.PollResultDTO;
import com.example.pollApp.dto.QuestionResultDTO;
import com.example.pollApp.entity.*;
import com.example.pollApp.exception.ResourceNotFoundException;
import com.example.pollApp.repository.ParticipationRepository;
import com.example.pollApp.repository.PollRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResultService {

    private final PollRepository pollRepository;
    private final ParticipationRepository participationRepository;

    @Transactional(readOnly = true)
    public PollResultDTO getAggregateResults(Long pollId) {
        Poll poll = pollRepository.findById(pollId)
                .orElseThrow(() -> new ResourceNotFoundException("Poll not found"));

        List<Participation> participations = participationRepository.findByPoll(poll);
        
        List<QuestionResultDTO> questionResults = poll.getQuestions().stream().map(question -> {
            List<Answer> answers = participations.stream()
                    .flatMap(p -> p.getAnswers().stream())
                    .filter(a -> a.getQuestion().getId().equals(question.getId()))
                    .collect(Collectors.toList());

            QuestionResultDTO result = QuestionResultDTO.builder()
                    .questionId(question.getId())
                    .content(question.getContent())
                    .build();

            if (question.getType() == QuestionType.TEXT) {
                result.setTextResponses(answers.stream().map(Answer::getContent).collect(Collectors.toList()));
            } else {
                Map<String, Long> distribution = answers.stream()
                        .collect(Collectors.groupingBy(Answer::getContent, Collectors.counting()));
                result.setDistribution(distribution);

                if (question.getType() == QuestionType.SCALE_1_TO_5) {
                    double avg = answers.stream()
                            .mapToInt(a -> Integer.parseInt(a.getContent()))
                            .average()
                            .orElse(0.0);
                    result.setAverage(avg);
                }
            }
            return result;
        }).collect(Collectors.toList());

        return PollResultDTO.builder()
                .pollId(poll.getId())
                .pollTitle(poll.getTitle())
                .questionResults(questionResults)
                .build();
    }
}
