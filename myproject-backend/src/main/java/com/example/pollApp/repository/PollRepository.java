package com.example.pollApp.repository;

import com.example.pollApp.entity.Poll;
import com.example.pollApp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface PollRepository extends JpaRepository<Poll, Long> {
    boolean existsByTitle(String title);
    boolean existsByTitleAndIdNot(String title, Long id);
    List<Poll> findByCreator(User creator);
    List<Poll> findByFinished(boolean finished);

    @Query("SELECT DISTINCT p FROM Poll p LEFT JOIN p.invitedUsers u WHERE p.creator.id = :userId OR u.id = :userId")
    List<Poll> findVisiblePolls(@Param("userId") Long userId);
}
