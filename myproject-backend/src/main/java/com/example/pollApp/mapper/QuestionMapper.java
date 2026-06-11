package com.example.pollApp.mapper;

import com.example.pollApp.dto.QuestionDTO;
import com.example.pollApp.entity.Question;
import org.springframework.stereotype.Component;

@Component
public class QuestionMapper {

    public QuestionDTO toDto(Question question) {
        if (question == null) return null;
        return QuestionDTO.builder()
                .id(question.getId())
                .content(question.getContent())
                .type(question.getType())
                .build();
    }

    public Question toEntity(QuestionDTO dto) {
        if (dto == null) return null;
        return Question.builder()
                .id(dto.getId())
                .content(dto.getContent())
                .type(dto.getType())
                .build();
    }
}
