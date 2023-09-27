package com.likesapp.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.likesapp.dto.Likes;
import com.likesapp.service.SpeakerService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SpeakerController {

    private final SpeakerService service;

    @PostMapping("/addlikes")
    public ResponseEntity<String> updateSpeaker(@RequestBody Likes likes) {
        try {
            service.addLikesToSpeaker(likes);
            return new ResponseEntity<>("Likes successfully added.", HttpStatus.ACCEPTED);
        } catch (Exception ex) {
            log.warn("Exception in controller:", ex);
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
        }
    }
}