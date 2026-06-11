package com.example.pollApp.service;

import com.example.pollApp.dto.InvitationDTO;
import com.example.pollApp.entity.Invitation;
import com.example.pollApp.entity.Poll;
import com.example.pollApp.exception.ResourceNotFoundException;
import com.example.pollApp.mapper.InvitationMapper;
import com.example.pollApp.repository.InvitationRepository;
import com.example.pollApp.repository.PollRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InvitationService {

    private final InvitationRepository invitationRepository;
    private final PollRepository pollRepository;
    private final InvitationMapper invitationMapper;

    @Transactional
    public InvitationDTO createInvitation(InvitationDTO invitationDTO) {
        Poll poll = pollRepository.findById(invitationDTO.getPollId())
                .orElseThrow(() -> new ResourceNotFoundException("Poll not found"));

        Invitation invitation = invitationMapper.toEntity(invitationDTO);
        invitation.setPoll(poll);

        return invitationMapper.toDto(invitationRepository.save(invitation));
    }

    @Transactional(readOnly = true)
    public List<InvitationDTO> getInvitationsByPoll(Long pollId) {
        Poll poll = pollRepository.findById(pollId)
                .orElseThrow(() -> new ResourceNotFoundException("Poll not found"));
        return invitationRepository.findByPoll(poll).stream()
                .map(invitationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<InvitationDTO> getPendingInvitationsForEmail(String email) {
        return invitationRepository.findByInviteeEmail(email).stream()
                .map(invitationMapper::toDto)
                .collect(Collectors.toList());
    }
}
