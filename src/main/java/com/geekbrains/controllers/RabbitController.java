package com.geekbrains.controllers;

import com.geekbrains.MarketApplication;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class RabbitController {
    private RabbitTemplate rabbitTemplate;

    @Autowired
    public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(String key) {
        System.out.println("Sending message...");
        rabbitTemplate.convertAndSend(MarketApplication.TOPIC_EXCHANGER_NAME, key, key);
    }
}
