package com.Fastlivery_Express.notification.listener;

import com.Fastlivery_Express.shipment.event.ShipmentEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ShipmentEventListener {

    @RabbitListener(queues = "${notifications.events.queue}")
    public void handleShipmentEvent(ShipmentEvent event) {
        log.info(
                "Notification requested for event={} shipmentId={} trackingNumber={} customerId={} driverId={} status={} paymentStatus={}",
                event.eventType(),
                event.shipmentId(),
                event.trackingNumber(),
                event.customerId(),
                event.driverId(),
                event.status(),
                event.paymentStatus()
        );
    }
}
