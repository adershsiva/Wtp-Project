package com.example.pollApp.dto;

import com.example.pollApp.entity.QuestionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionDTO {
    private Long id;

    @NotBlank(message = "Question content is required")
    private String content;

    @NotNull(message = "Question type is required")
    private QuestionType type;
}
