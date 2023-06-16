package com.bettervns.rabbitmq;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;

@Configuration
public class RabbitMQListener {

    public static final String SECURITY_QUEUE_NAME = "securityQueue";

    MessageProcessor processor;

    @Autowired
    public RabbitMQListener(MessageProcessor processor) {
        this.processor = processor;
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory factory = new CachingConnectionFactory("localhost");
        factory.setUsername("bettervns");
        factory.setPassword("bettervns");
        return factory;
    }

    @Bean
    public SimpleMessageListenerContainer messageListenerContainer(ConnectionFactory connectionFactory){
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(SECURITY_QUEUE_NAME);
        container.setMessageListener(message -> {
            String messageBody = new String(message.getBody(), StandardCharsets.UTF_8);
            processor.processMessage(messageBody);
        });
        return container;
    }
}