package com.example.pollApp.controller;

import com.example.pollApp.dto.PollDTO;
import com.example.pollApp.service.PollService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/polls")
@RequiredArgsConstructor
public class PollController {

    private final PollService pollService;

    @PostMapping
    public ResponseEntity<PollDTO> createPoll(@Valid @RequestBody PollDTO pollDTO, @RequestParam Long userId) {
        // userId is passed as a param for now since we skipped Phase 3
        return new ResponseEntity<>(pollService.createPoll(pollDTO, userId), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PollDTO> getPoll(@PathVariable Long id) {
        return ResponseEntity.ok(pollService.getPollById(id));
    }

    @GetMapping
    public ResponseEntity<List<PollDTO>> getAllPolls() {
        return ResponseEntity.ok(pollService.getAllPolls());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PollDTO> updatePoll(@PathVariable Long id, @Valid @RequestBody PollDTO pollDTO) {
        return ResponseEntity.ok(pollService.updatePoll(id, pollDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePoll(@PathVariable Long id) {
        pollService.deletePoll(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/finish")
    public ResponseEntity<PollDTO> finishPoll(@PathVariable Long id) {
        return ResponseEntity.ok(pollService.finishPoll(id));
    }
}
