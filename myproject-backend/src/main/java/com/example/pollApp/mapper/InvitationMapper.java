package com.example.pollApp.mapper;

import com.example.pollApp.dto.InvitationDTO;
import com.example.pollApp.entity.Invitation;
import org.springframework.stereotype.Component;

@Component
public class InvitationMapper {

    public InvitationDTO toDto(Invitation invitation) {
        if (invitation == null) return null;
        return InvitationDTO.builder()
                .id(invitation.getId())
                .pollId(invitation.getPoll() != null ? invitation.getPoll().getId() : null)
                .inviteeEmail(invitation.getInviteeEmail())
                .build();
    }

    public Invitation toEntity(InvitationDTO dto) {
        if (dto == null) return null;
        return Invitation.builder()
                .id(dto.getId())
                .inviteeEmail(dto.getInviteeEmail())
                .build();
    }
}
