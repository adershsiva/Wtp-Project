package com.example.pollApp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PollDTO {
    private Long id;

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    private boolean finished;

    private String creatorUsername;

    @NotEmpty(message = "Poll must have at least one question")
    private List<QuestionDTO> questions;
}
