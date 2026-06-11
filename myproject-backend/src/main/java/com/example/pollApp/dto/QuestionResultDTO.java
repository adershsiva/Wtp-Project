package com.example.pollApp.dto;

import lombok.*;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionResultDTO {
    private Long questionId;
    private String content;
    private Map<String, Long> distribution; // For BOOLEAN and SCALE
    private Double average; // For SCALE
    private java.util.List<String> textResponses; // For TEXT
}
