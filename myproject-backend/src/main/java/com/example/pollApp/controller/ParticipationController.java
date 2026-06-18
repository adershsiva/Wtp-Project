package com.example.pollApp.controller;

import com.example.pollApp.dto.ParticipationDTO;
import com.example.pollApp.dto.PollDTO;
import com.example.pollApp.mapper.PollMapper;
import com.example.pollApp.service.ParticipationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/participations")
@RequiredArgsConstructor
public class ParticipationController {

    private final ParticipationService participationService;
    private final PollMapper pollMapper;

    @PostMapping
    public ResponseEntity<ParticipationDTO> participate(@Valid @RequestBody ParticipationDTO participationDTO, @RequestParam Long userId) {
        return new ResponseEntity<>(participationService.participate(participationDTO, userId), HttpStatus.CREATED);
    }

    @GetMapping("/pending")
    public ResponseEntity<List<PollDTO>> getPendingPolls(@RequestParam Long userId) {
        return ResponseEntity.ok(participationService.getPendingPolls(userId).stream()
                .map(pollMapper::toDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParticipationDTO> getParticipation(@PathVariable Long id) {
        return ResponseEntity.ok(participationService.getParticipation(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ParticipationDTO>> getUserParticipations(@PathVariable Long userId) {
        return ResponseEntity.ok(participationService.getUserParticipations(userId));
    }
}
