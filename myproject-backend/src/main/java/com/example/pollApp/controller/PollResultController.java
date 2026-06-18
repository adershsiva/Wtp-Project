package com.example.pollApp.controller;

import com.example.pollApp.dto.ParticipationDTO;
import com.example.pollApp.dto.PollResultDTO;
import com.example.pollApp.service.ResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/results")
@RequiredArgsConstructor
public class PollResultController {

    private final ResultService resultService;

    @GetMapping("/poll/{pollId}/aggregate")
    public ResponseEntity<PollResultDTO> getAggregateResults(@PathVariable Long pollId, @RequestParam Long userId) {
        return ResponseEntity.ok(resultService.getAggregateResults(pollId, userId));
    }

    @GetMapping("/poll/{pollId}/individual")
    public ResponseEntity<List<ParticipationDTO>> getIndividualResponses(@PathVariable Long pollId, @RequestParam Long userId) {
        return ResponseEntity.ok(resultService.getIndividualResponses(pollId, userId));
    }
}
