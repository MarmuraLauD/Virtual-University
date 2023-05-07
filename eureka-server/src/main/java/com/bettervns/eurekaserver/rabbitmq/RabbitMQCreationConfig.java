package com.bettervns.eurekaserver.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQCreationConfig {

    private static final String STUDENTS_QUEUE_NAME = "studentsQueue";
    private static final String TEACHERS_QUEUE_NAME = "teachersQueue";
    private static final String DIRECT_EXCHANGE_NAME = "betterVNS-direct-exchange";
    private static final String RABBIT_HOST_NAME = "localhost";

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory factory = new CachingConnectionFactory(RABBIT_HOST_NAME);
        factory.setUsername("admin");
        factory.setPassword("grisha_sobaka1");
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
    Binding studentsQueueBinding(Queue studentsQueue, DirectExchange directExchange) {
        return BindingBuilder.bind(studentsQueue).to(directExchange).with(STUDENTS_QUEUE_NAME);
    }

    @Bean
    Binding teachersQueueBinding(Queue teachersQueue, DirectExchange directExchange) {
        return BindingBuilder.bind(teachersQueue).to(directExchange).with(TEACHERS_QUEUE_NAME);
    }
}