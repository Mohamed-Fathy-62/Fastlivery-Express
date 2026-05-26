package com.Fastlivery_Express.notification.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    @Bean
    public TopicExchange shipmentEventsExchange(@Value("${notifications.events.exchange}") String exchangeName) {
        return new TopicExchange(exchangeName);
    }

    @Bean
    public Queue shipmentNotificationsQueue(@Value("${notifications.events.queue}") String queueName) {
        return new Queue(queueName, true);
    }

    @Bean
    public Binding shipmentNotificationsBinding(Queue shipmentNotificationsQueue,
                                                TopicExchange shipmentEventsExchange,
                                                @Value("${notifications.events.routing-key}") String routingKey) {
        return BindingBuilder.bind(shipmentNotificationsQueue).to(shipmentEventsExchange).with(routingKey);
    }

    @Bean
    public MessageConverter jacksonJsonMessageConverter() {
        return new Jackson2JsonMessageConverter("com.Fastlivery_Express.*");
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory,
                                                                              MessageConverter messageConverter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter);
        return factory;
    }
}
