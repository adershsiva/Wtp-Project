package com.example.pollApp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvitationDTO {
    private Long id;

    private Long pollId;

    @NotBlank(message = "Invitee email is required")
    @Email(message = "Invitee email should be valid")
    private String inviteeEmail;
}
