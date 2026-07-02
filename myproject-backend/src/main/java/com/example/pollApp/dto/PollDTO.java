package com.example.pollApp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PollDTO {
    private Long id;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Due date is required")
    private LocalDateTime dueDate;

    private boolean finished;

    private boolean paused;

    private String creatorUsername;

    @NotEmpty(message = "Poll must have at least one question")
    private List<QuestionDTO> questions;

    private List<String> invitedUsernames;
}
