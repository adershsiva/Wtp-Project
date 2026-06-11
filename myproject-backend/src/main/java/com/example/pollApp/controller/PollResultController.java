package com.example.pollApp.controller;

import com.example.pollApp.dto.PollResultDTO;
import com.example.pollApp.service.ResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/results")
@RequiredArgsConstructor
public class PollResultController {

    private final ResultService resultService;

    @GetMapping("/poll/{pollId}")
    public ResponseEntity<PollResultDTO> getAggregateResults(@PathVariable Long pollId) {
        return ResponseEntity.ok(resultService.getAggregateResults(pollId));
    }
}
