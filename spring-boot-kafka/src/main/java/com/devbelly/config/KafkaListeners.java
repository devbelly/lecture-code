package com.devbelly.config;

import com.devbelly.PriceDownEventListener;
import com.devbelly.entity.Notification;
import com.devbelly.event.PriceDownEvent;
import com.devbelly.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class KafkaListeners {

    private final NotificationRepository notificationRepository;

    @KafkaListener(
            topics="price-down",
            containerFactory = "kafkaListenerContainerPriceDownFactory",
            groupId = "groupId"
    )
    void listener(PriceDownEvent events){
        System.out.println("okoko");
        notificationRepository.save(new Notification());
    }
}
