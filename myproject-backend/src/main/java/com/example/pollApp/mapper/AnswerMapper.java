package com.example.pollApp.mapper;

import com.example.pollApp.dto.AnswerDTO;
import com.example.pollApp.entity.Answer;
import org.springframework.stereotype.Component;

@Component
public class AnswerMapper {

    public AnswerDTO toDto(Answer answer) {
        if (answer == null) return null;
        return AnswerDTO.builder()
                .id(answer.getId())
                .questionId(answer.getQuestion() != null ? answer.getQuestion().getId() : null)
                .content(answer.getContent())
                .build();
    }

    public Answer toEntity(AnswerDTO dto) {
        if (dto == null) return null;
        return Answer.builder()
                .id(dto.getId())
                .content(dto.getContent())
                .build();
    }
}
