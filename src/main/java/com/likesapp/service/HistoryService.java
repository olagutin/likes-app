package com.likesapp.service;

import com.likesapp.repository.UsersRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.likesapp.dto.LikesDto;
import com.likesapp.entities.HistoryEntity;
import com.likesapp.repository.HistoryRepository;
import org.springframework.transaction.annotation.Propagation;

@Slf4j
@Service
@RequiredArgsConstructor
public class HistoryService {

    private final HistoryRepository historyRepository;
    private final UsersRepository usersRepository;

    /**
     * Method for saving message to history.
     * Produces the message with DTO to kafka, for future processing.
     *
     * @param likes DTO with information about likes to be added.
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveMessageToHistory(LikesDto likes, String status) {
        try {
            usersRepository.findById(1L).ifPresent(speaker -> {
                historyRepository.save(HistoryEntity.builder()
                        .nickName(likes.getNickName())
                        .likes(likes.getLikes())
                        .status(status)
                        .build());
            });
        } catch (RuntimeException ex) {
            log.warn("Failed to save message to history.", ex);
        }
    }
}
