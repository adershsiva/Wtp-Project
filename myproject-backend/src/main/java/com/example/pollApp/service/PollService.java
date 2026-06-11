package com.example.pollApp.service;

import com.example.pollApp.dto.PollDTO;
import com.example.pollApp.entity.Poll;
import com.example.pollApp.entity.User;
import com.example.pollApp.exception.ResourceNotFoundException;
import com.example.pollApp.mapper.PollMapper;
import com.example.pollApp.repository.PollRepository;
import com.example.pollApp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PollService {

    private final PollRepository pollRepository;
    private final UserRepository userRepository;
    private final PollMapper pollMapper;

    @Transactional
    public PollDTO createPoll(PollDTO pollDTO, Long creatorId) {
        User creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + creatorId));

        Poll poll = pollMapper.toEntity(pollDTO);
        poll.setCreator(creator);
        poll.setFinished(false);

        // Ensure bidirectional relationship for questions is set by the mapper 
        // (already handled in PollMapper.toEntity)

        Poll savedPoll = pollRepository.save(poll);
        return pollMapper.toDto(savedPoll);
    }

    @Transactional(readOnly = true)
    public PollDTO getPollById(Long id) {
        Poll poll = pollRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Poll not found with id: " + id));
        return pollMapper.toDto(poll);
    }

    @Transactional(readOnly = true)
    public List<PollDTO> getAllPolls() {
        return pollRepository.findAll().stream()
                .map(pollMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public PollDTO updatePoll(Long id, PollDTO pollDTO) {
        Poll existingPoll = pollRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Poll not found with id: " + id));

        existingPoll.setTitle(pollDTO.getTitle());
        existingPoll.setDescription(pollDTO.getDescription());
        
        // Note: Simple update. Complex question updates might require more logic 
        // to handle removing/adding specific questions.
        
        Poll updatedPoll = pollRepository.save(existingPoll);
        return pollMapper.toDto(updatedPoll);
    }

    @Transactional
    public void deletePoll(Long id) {
        if (!pollRepository.existsById(id)) {
            throw new ResourceNotFoundException("Poll not found with id: " + id);
        }
        pollRepository.deleteById(id);
    }

    @Transactional
    public PollDTO finishPoll(Long id) {
        Poll poll = pollRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Poll not found with id: " + id));
        poll.setFinished(true);
        return pollMapper.toDto(pollRepository.save(poll));
    }
}
