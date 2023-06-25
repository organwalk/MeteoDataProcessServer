package com.weather.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Value("${spring.rabbitmq.host}")
    private String rabbitMQHost;

    @Value("${spring.rabbitmq.port}")
    private int rabbitMQPort;

    @Value("${spring.rabbitmq.username}")
    private String rabbitMQUsername;

    @Value("${spring.rabbitmq.password}")
    private String rabbitMQPassword;

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(rabbitMQHost);
        connectionFactory.setPort(rabbitMQPort);
        connectionFactory.setUsername(rabbitMQUsername);
        connectionFactory.setPassword(rabbitMQPassword);
        return connectionFactory;
    }

    @Bean
    public FanoutExchange udpRequestExchange() {
        return new FanoutExchange("udp-req-exchange");
    }

    @Bean
    public Queue udpRequestQueue() {
        return new Queue("udp-req-queue");
    }

    @Bean
    public Binding udpRequestBinding() {
        return BindingBuilder.bind(udpRequestQueue()).to(udpRequestExchange());
    }

    @Bean
    public FanoutExchange udpResponseExchange() {
        return new FanoutExchange("udp-res-exchange");
    }

    @Bean
    public Queue udpResponseQueue() {
        return new Queue("udp-res-queue");
    }

    @Bean
    public Binding udpResponseBinding() {
        return BindingBuilder.bind(udpResponseQueue()).to(udpResponseExchange());
    }
    @Bean
    public FanoutExchange taskStatusExchange() {
        return new FanoutExchange("task-over-exchange");
    }

    @Bean
    public Queue taskStatusQueue() {
        return new Queue("task-over-queue");
    }

    @Bean
    public Binding taskStatusBinding() {
        return BindingBuilder.bind(taskStatusQueue()).to(taskStatusExchange());
    }
}
