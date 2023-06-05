package com.bettervns.studyingservice.rabbitmq;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQEntityListener {
    public static final String STUDYING_ENTITY_QUEUE_NAME = "studyingEntityQueue";
    EntityMessageProcessor processor;

    @Autowired
    public RabbitMQEntityListener(EntityMessageProcessor processor) {
        this.processor = processor;
    }

    @Bean
    public SimpleMessageListenerContainer entityListenerContainer(ConnectionFactory connectionFactory){
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(STUDYING_ENTITY_QUEUE_NAME);
        container.setMessageListener(message -> {
            processor.processMessage(new String(message.getBody()));
        });
        return container;
    }
}