package com.example.pollApp.controller;

import com.example.pollApp.dto.ParticipationDTO;
import com.example.pollApp.service.ParticipationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/participations")
@RequiredArgsConstructor
public class ParticipationController {

    private final ParticipationService participationService;

    @PostMapping
    public ResponseEntity<ParticipationDTO> participate(@Valid @RequestBody ParticipationDTO participationDTO, @RequestParam Long userId) {
        return new ResponseEntity<>(participationService.participate(participationDTO, userId), HttpStatus.CREATED);
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
