package com.example.pollApp.repository;

import com.example.pollApp.entity.Invitation;
import com.example.pollApp.entity.Poll;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface InvitationRepository extends JpaRepository<Invitation, Long> {
    List<Invitation> findByInviteeEmail(String inviteeEmail);
    List<Invitation> findByPoll(Poll poll);
}
