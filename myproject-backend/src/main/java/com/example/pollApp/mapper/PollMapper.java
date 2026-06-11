package com.example.pollApp.mapper;

import com.example.pollApp.dto.PollDTO;
import com.example.pollApp.entity.Poll;
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
                .finished(poll.isFinished())
                .creatorUsername(poll.getCreator() != null ? poll.getCreator().getUsername() : null)
                .questions(poll.getQuestions() != null ? 
                        poll.getQuestions().stream().map(questionMapper::toDto).collect(Collectors.toList()) : null)
                .build();
    }

    public Poll toEntity(PollDTO dto) {
        if (dto == null) return null;
        Poll poll = Poll.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .finished(dto.isFinished())
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
