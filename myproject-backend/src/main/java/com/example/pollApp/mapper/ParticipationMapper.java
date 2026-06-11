package com.example.pollApp.mapper;

import com.example.pollApp.dto.ParticipationDTO;
import com.example.pollApp.entity.Participation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ParticipationMapper {

    private final AnswerMapper answerMapper;

    public ParticipationDTO toDto(Participation participation) {
        if (participation == null) return null;
        return ParticipationDTO.builder()
                .id(participation.getId())
                .pollId(participation.getPoll() != null ? participation.getPoll().getId() : null)
                .username(participation.getUser() != null ? participation.getUser().getUsername() : null)
                .answers(participation.getAnswers() != null ?
                        participation.getAnswers().stream().map(answerMapper::toDto).collect(Collectors.toList()) : null)
                .build();
    }

    public Participation toEntity(ParticipationDTO dto) {
        if (dto == null) return null;
        Participation participation = Participation.builder()
                .id(dto.getId())
                .build();
        
        if (dto.getAnswers() != null) {
            participation.setAnswers(dto.getAnswers().stream()
                    .map(aDto -> {
                        var a = answerMapper.toEntity(aDto);
                        a.setParticipation(participation);
                        return a;
                    }).collect(Collectors.toList()));
        }
        return participation;
    }
}
