package com.debttrackr.service;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Service;

//@Service
//public class DebtConsumer {

//    @RabbitListener(queues = RabbitMQConfig.QUEUE)
//    public void consume(String message) {
//        System.out.println("📩 Consumed: " + message);
//
//    }



    @Service
    public class DebtConsumer implements ChannelAwareMessageListener {

        @Override
        public void onMessage(Message message, Channel channel) throws Exception {

            // get routing key - know which queue sent message
            String routingKey = message.getMessageProperties().getReceivedRoutingKey();

            // get queue name
            String queueName = message.getMessageProperties().getConsumerQueue();

            // get body as string
            String body = new String(message.getBody());

            System.out.println("📩 Queue: " + queueName);
            System.out.println("📩 RoutingKey: " + routingKey);
            System.out.println("📩 Message: " + body);

            // handle based on routing key - fully generic!
            switch (routingKey) {
                case "debt.created"    -> handleDebt(body);
                case "order.created"   -> handleOrder(body);
                case "payment.created" -> handlePayment(body);
                default -> System.out.println("Unknown routing key: " + routingKey);
            }

            // manual acknowledge
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }

        private void handleDebt(String body) {
            System.out.println("Handling debt: " + body);
        }

        private void handleOrder(String body) {
            System.out.println("Handling order: " + body);
        }

        private void handlePayment(String body) {
            System.out.println("Handling payment: " + body);
        }
    }

