package com.example.pollApp.mapper;

import com.example.pollApp.dto.PollDTO;
import com.example.pollApp.entity.Poll;
import com.example.pollApp.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PollMapper {

    private final QuestionMapper questionMapper;

    public PollDTO toDto(Poll poll) {
        if (poll == null) return null;
        return PollDTO.builder()
                .id(poll.getId())
                .title(poll.getTitle())
                .description(poll.getDescription())
                .dueDate(poll.getDueDate())
                .finished(poll.isFinished())
                .paused(poll.isPaused())
                .creatorUsername(poll.getCreator() != null ? poll.getCreator().getUsername() : null)
                .questions(poll.getQuestions() != null ? 
                        poll.getQuestions().stream().map(questionMapper::toDto).collect(Collectors.toList()) : null)
                .invitedUsernames(poll.getInvitedUsers() != null ?
                        poll.getInvitedUsers().stream().map(User::getUsername).collect(Collectors.toList()) : null)
                .build();
    }

    public Poll toEntity(PollDTO dto) {
        if (dto == null) return null;
        Poll poll = Poll.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .dueDate(dto.getDueDate())
                .finished(dto.isFinished())
                .paused(dto.isPaused())
                .build();
        
        if (dto.getQuestions() != null) {
            poll.setQuestions(dto.getQuestions().stream()
                    .map(qDto -> {
                        var q = questionMapper.toEntity(qDto);
                        q.setPoll(poll);
                        return q;
                    }).collect(Collectors.toList()));
        }
        return poll;
    }
}
