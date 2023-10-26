package com.likesapp.service;

import org.springframework.retry.annotation.Recover;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import com.likesapp.dto.LikesDto;
import com.likesapp.entities.HistoryEntity;
import com.likesapp.repository.HistoryRepository;
import com.likesapp.repository.UsersRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UsersRepository usersRepository;
    private final HistoryService historyService;
    private final StreamBridge streamBridge;

    /**
     * Method for adding likes to user by ID or NickName.
     *
     * @param likes DTO with information about likes to be added.
     */
    @Transactional(timeout = 10)
    public void addLikesToUser(LikesDto likes) {
        if (likes.getNickName() != null) {
            usersRepository.findByNickName(likes.getNickName()).ifPresentOrElse(user -> {
                saveMessageToHistory(likes, "RECEIVED");
                user.setLikes(user.getLikes() + likes.getLikes());
                usersRepository.save(user);
                log.info("{} likes added to {}", likes.getLikes(), user.getFirstName() + " " + user.getLastName());
            }, () -> {
                log.warn("User with nickname {} not found", likes.getNickName());
                saveMessageToHistory(likes, "ORPHANED");
            });
        } else {
            log.error("Error during adding likes, no IDs given");
            saveMessageToHistory(likes, "CORRUPTED");
        }
    }

    //TODO the method should be optimized for the recovery process
    public void addLikesToUserRecover(RuntimeException ex, LikesDto likes){
        if (likes.getNickName() != null) {
            usersRepository.findByNickName(likes.getNickName()).ifPresentOrElse(user -> {
                user.setLikes(user.getLikes() + likes.getLikes());
                log.info("{} likes added to {}", likes.getLikes(), user.getFirstName() + " " + user.getLastName());
            }, () -> {
                log.warn("User with nickname {} not found", likes.getNickName());
            });
        } else {
            log.error("Error during adding likes, no IDs given");
        }
    }

    /**
     * Method for creating task to add likes to user.
     * Produces the message with DTO to kafka, for future processing.
     *
     * @param likes DTO with information about likes to be added.
     */
    public void createTaskToAddLikes(LikesDto likes) {
        streamBridge.send("likesProducer-out-0", likes);
    }

    /**
     * Method for saving message to history.
     * Produces the message with DTO to kafka, for future processing.
     *
     * @param likes DTO with information about likes to be added.
     */
    private void saveMessageToHistory(LikesDto likes, String status) {
        historyService.saveMessageToHistory(likes, status);
    }
}
