package com.bettervns.eurekaserver.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQCreationConfig {

    private static final String STUDENTS_QUEUE_NAME = "studentsQueue";
    private static final String SECURITY_QUEUE_NAME = "securityQueue";
    private static final String TEACHERS_QUEUE_NAME = "teachersQueue";
    private static final String STUDYING_ENTITY_QUEUE_NAME = "studyingEntityQueue";
    private static final String STUDYING_ATTACHMENT_QUEUE_NAME = "studyingAttachmentQueue";
    private static final String DIRECT_EXCHANGE_NAME = "betterVNS-direct-exchange";
    private static final String RABBIT_HOST_NAME = "localhost";

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory factory = new CachingConnectionFactory(RABBIT_HOST_NAME);
        factory.setUsername("bettervns");
        factory.setPassword("bettervns");
        return factory;
    }

    @Bean
    DirectExchange directExchange() {
        return new DirectExchange(DIRECT_EXCHANGE_NAME);
    }

    @Bean
    Queue studentsQueue() {
        return new Queue(STUDENTS_QUEUE_NAME, false);
    }

    @Bean
    Queue teachersQueue() {
        return new Queue(TEACHERS_QUEUE_NAME, false);
    }

    @Bean
    Queue securityQueue() {
        return new Queue(SECURITY_QUEUE_NAME, false);
    }

    @Bean
    Queue studyingEntityQueue() {
        return new Queue(STUDYING_ENTITY_QUEUE_NAME, false);
    }

    @Bean
    Queue studyingAttachmentQueue() {
        return new Queue(STUDYING_ATTACHMENT_QUEUE_NAME, false);
    }

    @Bean
    Binding studentsQueueBinding(Queue studentsQueue, DirectExchange directExchange) {
        return BindingBuilder.bind(studentsQueue).to(directExchange).with(STUDENTS_QUEUE_NAME);
    }

    @Bean
    Binding securityQueueBinding(Queue securityQueue, DirectExchange directExchange) {
        return BindingBuilder.bind(securityQueue).to(directExchange).with(SECURITY_QUEUE_NAME);
    }

    @Bean
    Binding teachersQueueBinding(Queue teachersQueue, DirectExchange directExchange) {
        return BindingBuilder.bind(teachersQueue).to(directExchange).with(TEACHERS_QUEUE_NAME);
    }

    @Bean
    Binding studyingQueueBinding(Queue studyingEntityQueue, DirectExchange directExchange) {
        return BindingBuilder.bind(studyingEntityQueue).to(directExchange).with(STUDYING_ENTITY_QUEUE_NAME);
    }

    @Bean
    Binding studyingAttachmentBinding(Queue studyingAttachmentQueue, DirectExchange directExchange) {
        return BindingBuilder.bind(studyingAttachmentQueue).to(directExchange).with(STUDYING_ATTACHMENT_QUEUE_NAME);
    }
}