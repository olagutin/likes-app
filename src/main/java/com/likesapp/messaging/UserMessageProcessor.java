package com.likesapp.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.likesapp.dto.LikesDto;
import com.likesapp.service.UserService;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserMessageProcessor {

    private final UserService speakerService;

    public void processOneMessage(LikesDto likes) {
        speakerService.addLikesToUser(likes);
    }

    //<editor-fold desc="Batch Processing">
    public void processBatchOfMessages(List<LikesDto> likes) {

        var accumulatedLikes = likes.stream()
                .filter(Objects::nonNull)
                .filter(x -> x.getNickName() != null)
                .filter(x -> !x.getNickName().isEmpty())
                .collect(Collectors.groupingBy(LikesDto::getNickName))
                .values().stream()
                .map(likesListNickName -> likesListNickName.stream().reduce(new LikesDto(), (x, y) -> LikesDto.builder()
                        .nickName(y.getNickName())
                        .likes(x.getLikes() + y.getLikes())
                        .build()))
                .collect(Collectors.toList());
        log.info("Aggregated Likes: {}", accumulatedLikes);

        try {
            var futures = accumulatedLikes.stream()
                    .map(like -> CompletableFuture.runAsync(() -> speakerService.addLikesToUser(like)))
                    .toArray(CompletableFuture[]::new);
            CompletableFuture.allOf(futures).join();
        } catch (CompletionException ex) {
            log.error("Something went wrong during batch processing.:", ex);
        }
    }
    //</editor-fold>
}
