package com.example.pollApp.repository;

import com.example.pollApp.entity.Participation;
import com.example.pollApp.entity.Poll;
import com.example.pollApp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {
    List<Participation> findByUser(User user);
    List<Participation> findByPoll(Poll poll);
    Optional<Participation> findByPollAndUser(Poll poll, User user);
}
