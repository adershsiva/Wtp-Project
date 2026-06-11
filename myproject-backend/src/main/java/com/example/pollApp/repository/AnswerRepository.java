package com.example.pollApp.repository;

import com.example.pollApp.entity.Answer;
import com.example.pollApp.entity.Participation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findByParticipation(Participation participation);
}
