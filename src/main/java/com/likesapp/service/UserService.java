package com.likesapp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;
import com.likesapp.dto.Likes;
import com.likesapp.entities.HistoryEntity;
import com.likesapp.repository.HistoryRepository;
import com.likesapp.repository.UsersRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UsersRepository usersRepository;
    private final HistoryRepository historyRepository;
    private final StreamBridge streamBridge;

    /**
     * Method for adding likes to speaker by ID or NickName.
     *
     * @param likes DTO with information about likes to be added.
     */
    public void addLikesToUser(Likes likes) {
        if (likes.getNickName() != null) {
            usersRepository.findByNickName(likes.getNickName()).ifPresentOrElse(speaker -> {
                saveMessageToHistory(likes, "RECEIVED");
                speaker.setLikes(speaker.getLikes() + likes.getLikes());
                usersRepository.save(speaker);
                log.info("{} likes added to {}", likes.getLikes(), speaker.getFirstName() + " " + speaker.getLastName());
            }, () -> {
                log.warn("User with talk {} not found", likes.getNickName());
                saveMessageToHistory(likes, "ORPHANED");
            });
        } else {
            log.error("Error during adding likes, no IDs given");
            saveMessageToHistory(likes, "CORRUPTED");
        }
    }

    /**
     * Method for creating task to add likes to speaker.
     * Produces the message with DTO to kafka, for future processing.
     *
     * @param likes DTO with information about likes to be added.
     */
    public void createTaskToAddLikes(Likes likes) {
        streamBridge.send("likesProducer-out-0", likes);
    }

    /**
     * Method for saving message to history.
     * Produces the message with DTO to kafka, for future processing.
     *
     * @param likes DTO with information about likes to be added.
     */
    private void saveMessageToHistory(Likes likes, String status) {
        try {
            historyRepository.save(HistoryEntity.builder()
                    .nickName(likes.getNickName())
                    .likes(likes.getLikes())
                    .status(status)
                    .build());
        } catch (RuntimeException ex) {
            log.warn("Failed to save message to history.", ex);
        }
    }
}
