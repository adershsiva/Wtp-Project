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
        return new ResponseEntity<>(pollService.createPoll(pollDTO, userId), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PollDTO> getPoll(@PathVariable Long id) {
        return ResponseEntity.ok(pollService.getPollById(id));
    }

    @GetMapping
    public ResponseEntity<List<PollDTO>> getAllPolls(@RequestParam(required = false) Long userId) {
        return ResponseEntity.ok(pollService.getAllPolls(userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PollDTO> updatePoll(@PathVariable Long id, @Valid @RequestBody PollDTO pollDTO, @RequestParam Long userId) {
        return ResponseEntity.ok(pollService.updatePoll(id, pollDTO, userId));
    }

    @PostMapping("/{id}/invite")
    public ResponseEntity<PollDTO> inviteUser(@PathVariable Long id, @RequestParam String username, @RequestParam Long userId) {
        return ResponseEntity.ok(pollService.inviteUser(id, username, userId));
    }

    @PatchMapping("/{id}/finish")
    public ResponseEntity<PollDTO> finishPoll(@PathVariable Long id, @RequestParam Long userId) {
        return ResponseEntity.ok(pollService.finishPoll(id, userId));
    }

    @PatchMapping("/{id}/pause")
    public ResponseEntity<PollDTO> pausePoll(@PathVariable Long id, @RequestParam Long userId) {
        return ResponseEntity.ok(pollService.pausePoll(id, userId));
    }

    @PatchMapping("/{id}/resume")
    public ResponseEntity<PollDTO> resumePoll(@PathVariable Long id, @RequestParam Long userId) {
        return ResponseEntity.ok(pollService.resumePoll(id, userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePoll(@PathVariable Long id, @RequestParam Long userId) {
        pollService.deletePoll(id, userId);
        return ResponseEntity.noContent().build();
    }
}
