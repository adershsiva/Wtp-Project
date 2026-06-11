package com.example.pollApp.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParticipationDTO {
    private Long id;

    @NotNull(message = "Poll ID is required")
    private Long pollId;

    private String username;

    @NotEmpty(message = "Participation must include answers to questions")
    private List<AnswerDTO> answers;
}
