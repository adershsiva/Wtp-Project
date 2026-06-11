package com.example.pollApp.repository;

import com.example.pollApp.entity.Question;
import com.example.pollApp.entity.Poll;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByPoll(Poll poll);
}
