package com.project.InsureCompare.infra.messaging;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String REQUEST_QUEUE = "policy-approval-request";
    public static final String RESPONSE_QUEUE = "policy-approval-response";
    public static final String EXCHANGE = "policy-approval-exchange";
    public static final String ROUTING_KEY_REQUEST = "policy.approval.request";
    public static final String ROUTING_KEY_RESPONSE = "policy.approval.response";

    @Bean
    public Queue requestQueue() {
        return new Queue(REQUEST_QUEUE, true);
    }

    @Bean
    public Queue responseQueue() {
        return new Queue(RESPONSE_QUEUE, true);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Binding requestBinding(Queue requestQueue, TopicExchange exchange) {
        return BindingBuilder.bind(requestQueue).to(exchange).with(ROUTING_KEY_REQUEST);
    }

    @Bean
    public Binding responseBinding(Queue responseQueue, TopicExchange exchange) {
        return BindingBuilder.bind(responseQueue).to(exchange).with(ROUTING_KEY_RESPONSE);
    }
}