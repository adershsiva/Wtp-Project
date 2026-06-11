package com.example.pollApp.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PollResultDTO {
    private Long pollId;
    private String pollTitle;
    private List<QuestionResultDTO> questionResults;
}
