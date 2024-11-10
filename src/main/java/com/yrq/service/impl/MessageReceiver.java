package com.yrq.service.impl;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class MessageReceiver {

    @RabbitListener(queues = "queueName")
    public void receiveMessage(String message) {
        System.out.println("Received message: " + message);
        // 处理接收到的消息
    }
}
