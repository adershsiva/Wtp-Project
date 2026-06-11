package com.example.pollApp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnswerDTO {
    private Long id;

    @NotNull(message = "Question ID is required")
    private Long questionId;

    @NotBlank(message = "Answer content is required")
    private String content;
}
