package com.debttrackr.service;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE    = "debt.exchange";
    public static final String QUEUE       = "debt.queue";
    public static final String ROUTING_KEY = "debt.created";

    @Bean
    public Queue queue() {
        System.out.println("✅ QUEUE BEAN CREATED: " + QUEUE);
        return new Queue(QUEUE, true);
    }

    @Bean
    public DirectExchange exchange() {
        System.out.println("✅ EXCHANGE BEAN CREATED: " + EXCHANGE);
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    public Binding binding(Queue queue, DirectExchange exchange) {
        System.out.println("✅ BINDING BEAN CREATED");
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with(ROUTING_KEY);
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        System.out.println("✅ RABBIT ADMIN BEAN CREATED");
        RabbitAdmin admin = new RabbitAdmin(connectionFactory);
        admin.setAutoStartup(true);  // ← add this!

        // force immediate connection
        connectionFactory.createConnection();  // ← add this!
        System.out.println("✅ CONNECTION FORCED");
        return admin;
    }




    @Bean
    public SimpleMessageListenerContainer listenerContainer(
            ConnectionFactory connectionFactory,
            ChannelAwareMessageListener debtConsumer) {

        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);

        // ← add all queues here — fully generic!
        container.setQueueNames("debt.queue");

        container.setMessageListener(debtConsumer);
        return container;
    }



}