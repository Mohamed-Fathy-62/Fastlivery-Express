package com.Fastlivery_Express.shipment.event;

import com.Fastlivery_Express.shipment.entity.Shipment;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ShipmentEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${shipments.events.exchange}")
    private String exchange;

    @Value("${shipments.events.routing-key}")
    private String routingKey;

    public void publish(String eventType, Shipment shipment) {
        ShipmentEvent event = new ShipmentEvent(
                eventType,
                shipment.getId(),
                shipment.getTrackingNumber(),
                shipment.getCustomerId(),
                shipment.getDriverId(),
                shipment.getStatus(),
                shipment.getPaymentStatus(),
                LocalDateTime.now()
        );
        rabbitTemplate.convertAndSend(exchange, routingKey, event);
    }
}
