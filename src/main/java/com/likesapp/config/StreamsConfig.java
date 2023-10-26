//package com.likesapp.config;
//
//import com.likesapp.messaging.UserMessageProcessor;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import com.likesapp.dto.LikesDto;
//
//import java.util.function.Consumer;
//
//@Slf4j
//@Configuration
//@RequiredArgsConstructor
//public class StreamsConfig {
//
//    private final UserMessageProcessor messageProcessor;
//
//    @Bean
//    Consumer<LikesDto> LikesConsumer() {
//        return (value) -> {
//            log.info("Consumer Received : " + value);
//            messageProcessor.processOneMessage(value);
//        };
//    }
//}