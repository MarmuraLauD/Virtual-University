package com.bettervns.studyingservice.rabbitmq;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQAttachmentListener {
    public static final String STUDYING_ATTACHMENT_QUEUE_NAME = "studyingAttachmentQueue";
    EntityMessageProcessor processor;

    @Autowired
    public RabbitMQAttachmentListener(EntityMessageProcessor processor) {
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
        container.setQueueNames(STUDYING_ATTACHMENT_QUEUE_NAME);
        container.setMessageListener(message -> {
            processor.processMessage(new String(message.getBody()));
        });
        return container;
    }
}