package com.bettervns.teachersservice.rabbitmq;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQListener {

    public static final String TEACHERS_QUEUE_NAME = "teachersQueue";

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
        container.setQueueNames(TEACHERS_QUEUE_NAME);
        container.setMessageListener(message -> {
            System.out.println("Received from Queue: " + message.getBody());
            processor.processMessage(new String(message.getBody()));
        });
        return container;
    }
}