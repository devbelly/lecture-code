package com.devbelly.config;

import com.devbelly.event.PriceDownEvent;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    public Map<String,Object> producerConfig(){
        HashMap<String,Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class); // key will string
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class); // value will string
        return props;
    }

    /**
     * producer Factory, responsible for creating producer instances
     */
    @Bean
    public ProducerFactory<String, PriceDownEvent> producerFactory(){
        return new DefaultKafkaProducerFactory<>(producerConfig());
    }

    /**
     * send message, this is possible with kafkaTemplate
     * producerFactory를 인자로 가진다
     */
    @Bean
    @Qualifier("kafkaPriceDownTemplate")
    public KafkaTemplate<String, PriceDownEvent> kafkaTemplate(ProducerFactory<String,PriceDownEvent> producerFactory){
        return new KafkaTemplate<>(producerFactory);
    }

}
