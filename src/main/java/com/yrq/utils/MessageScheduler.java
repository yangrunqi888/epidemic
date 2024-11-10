package com.yrq.utils;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class MessageScheduler {

    private final MessageWebSocketHandler messageWebSocketHandler;

    public MessageScheduler(MessageWebSocketHandler messageWebSocketHandler) {
        this.messageWebSocketHandler = messageWebSocketHandler;
    }

    @Scheduled(fixedRate = 3000)
    public void sendPeriodicMessage() {
        String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String message;

        int currentHour = LocalDateTime.now().getHour();
        if (currentHour >= 20 || currentHour < 6) {
            message = "亲爱的居民朋友，夜深了，该睡了！";
            messageWebSocketHandler.sendMessageToAll(message);
        }


    }
}