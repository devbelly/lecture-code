package com.devbelly;


import com.devbelly.event.PriceDownEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import jdk.jfr.Event;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;

public class PriceDownEventListener implements AutoCloseable{

    @Value("price-down")
    private String priceDownTopic;

    private final EventBus eventBus;
    private final KafkaTemplate<String,PriceDownEvent> kafkaTemplate;

    public PriceDownEventListener(EventBus eventBus, KafkaTemplate<String, PriceDownEvent> kafkaTemplate){
        this.eventBus=eventBus;
        this.kafkaTemplate=kafkaTemplate;
        eventBus.register(this);
    }

    @Subscribe
    public void getPriceDownEvent(PriceDownEvent priceDownEvent){

        kafkaTemplate.send(priceDownTopic,priceDownEvent);
    }

    @Override
    public void close() throws Exception {
        eventBus.unregister(this);
    }
}
