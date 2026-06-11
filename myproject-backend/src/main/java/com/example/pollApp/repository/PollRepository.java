package com.example.pollApp.repository;

import com.example.pollApp.entity.Poll;
import com.example.pollApp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PollRepository extends JpaRepository<Poll, Long> {
    List<Poll> findByCreator(User creator);
    List<Poll> findByFinished(boolean finished);
}
