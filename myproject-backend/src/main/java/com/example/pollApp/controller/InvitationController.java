package com.example.pollApp.controller;

import com.example.pollApp.dto.InvitationDTO;
import com.example.pollApp.service.InvitationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invitations")
@RequiredArgsConstructor
public class InvitationController {

    private final InvitationService invitationService;

    @PostMapping
    public ResponseEntity<InvitationDTO> createInvitation(@Valid @RequestBody InvitationDTO invitationDTO) {
        return new ResponseEntity<>(invitationService.createInvitation(invitationDTO), HttpStatus.CREATED);
    }

    @GetMapping("/poll/{pollId}")
    public ResponseEntity<List<InvitationDTO>> getInvitationsByPoll(@PathVariable Long pollId) {
        return ResponseEntity.ok(invitationService.getInvitationsByPoll(pollId));
    }

    @GetMapping("/pending")
    public ResponseEntity<List<InvitationDTO>> getPendingInvitations(@RequestParam String email) {
        return ResponseEntity.ok(invitationService.getPendingInvitationsForEmail(email));
    }
}
