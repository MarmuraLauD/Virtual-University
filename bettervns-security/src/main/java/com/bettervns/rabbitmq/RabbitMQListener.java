package com.bettervns.rabbitmq;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQListener {
    public static final String SECURITY_QUEUE_NAME = "securityQueue";
    MessageProcessor processor;

    @Autowired
    public RabbitMQListener(MessageProcessor processor) {
        this.processor = processor;
    }

    @Bean
    public SimpleMessageListenerContainer entityListenerContainer(ConnectionFactory connectionFactory){
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(SECURITY_QUEUE_NAME);
        container.setMessageListener(message -> {
            processor.processMessage(new String(message.getBody()));
        });
        return container;
    }
}