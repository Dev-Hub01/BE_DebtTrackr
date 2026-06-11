package com.debttrackr.service;


import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DebtPublisher {

    @Autowired
    RabbitTemplate rabbitTemplate;

    public void publish(String message) {
        System.out.println("✅ Publishing: " + message);
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.ROUTING_KEY,
                message
        );
    }




}
