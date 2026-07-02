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
        if (pollRepository.existsByTitle(pollDTO.getTitle())) {
            throw new IllegalStateException("A poll with the exact same title already exists.");
        }

        User creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + creatorId));

        Poll poll = pollMapper.toEntity(pollDTO);
        poll.setCreator(creator);
        poll.setFinished(false);

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
    public List<PollDTO> getAllPolls(Long userId) {
        if (userId == null) {
            return List.of();
        }
        return pollRepository.findVisiblePolls(userId).stream()
                .map(pollMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public PollDTO updatePoll(Long id, PollDTO pollDTO, Long userId) {
        Poll existingPoll = pollRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Poll not found with id: " + id));

        if (!existingPoll.getCreator().getId().equals(userId)) {
            throw new IllegalStateException("Only the creator can update the poll.");
        }

        if (pollRepository.existsByTitleAndIdNot(pollDTO.getTitle(), id)) {
            throw new IllegalStateException("A poll with the exact same title already exists.");
        }

        existingPoll.setTitle(pollDTO.getTitle());
        existingPoll.setDescription(pollDTO.getDescription());
        existingPoll.setDueDate(pollDTO.getDueDate());
        
        Poll updatedPoll = pollRepository.save(existingPoll);
        return pollMapper.toDto(updatedPoll);
    }

    @Transactional
    public PollDTO inviteUser(Long pollId, String username, Long creatorId) {
        Poll poll = pollRepository.findById(pollId)
                .orElseThrow(() -> new ResourceNotFoundException("Poll not found"));
        
        if (!poll.getCreator().getId().equals(creatorId)) {
            throw new IllegalStateException("Only the creator can invite users.");
        }

        User userToInvite = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));

        if (userToInvite.getId().equals(creatorId)) {
            throw new IllegalStateException("A creator cannot invite themselves to their own poll.");
        }

        if (!poll.getInvitedUsers().contains(userToInvite)) {
            poll.getInvitedUsers().add(userToInvite);
        }

        return pollMapper.toDto(pollRepository.save(poll));
    }

    @Transactional
    public PollDTO finishPoll(Long id, Long creatorId) {
        Poll poll = pollRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Poll not found with id: " + id));
        
        if (!poll.getCreator().getId().equals(creatorId)) {
            throw new IllegalStateException("Only the creator can finish the poll.");
        }

        poll.setFinished(true);
        return pollMapper.toDto(pollRepository.save(poll));
    }

    @Transactional
    public PollDTO pausePoll(Long id, Long creatorId) {
        Poll poll = pollRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Poll not found with id: " + id));

        if (!poll.getCreator().getId().equals(creatorId)) {
            throw new IllegalStateException("Only the creator can pause the poll.");
        }

        if (poll.isFinished()) {
            throw new IllegalStateException("Cannot pause a finished poll.");
        }

        poll.setPaused(true);
        return pollMapper.toDto(pollRepository.save(poll));
    }

    @Transactional
    public PollDTO resumePoll(Long id, Long creatorId) {
        Poll poll = pollRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Poll not found with id: " + id));

        if (!poll.getCreator().getId().equals(creatorId)) {
            throw new IllegalStateException("Only the creator can resume the poll.");
        }

        if (poll.isFinished()) {
            throw new IllegalStateException("Cannot resume a finished poll.");
        }

        poll.setPaused(false);
        return pollMapper.toDto(pollRepository.save(poll));
    }

    @Transactional
    public void deletePoll(Long id, Long creatorId) {
        Poll poll = pollRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Poll not found with id: " + id));

        if (!poll.getCreator().getId().equals(creatorId)) {
            throw new IllegalStateException("Only the creator can delete the poll.");
        }

        pollRepository.delete(poll);
    }
}
