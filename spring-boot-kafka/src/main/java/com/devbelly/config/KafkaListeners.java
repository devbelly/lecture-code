package com.devbelly.config;

import com.devbelly.PriceDownEventListener;
import com.devbelly.entity.Notification;
import com.devbelly.event.PriceDownEvent;
import com.devbelly.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaListeners {

    private final NotificationRepository notificationRepository;

    @KafkaListener(
            topics="price-down",
            groupId = "groupId"
    )
    void listener(PriceDownEvent event){
        notificationRepository.save(new Notification());
    }
}
