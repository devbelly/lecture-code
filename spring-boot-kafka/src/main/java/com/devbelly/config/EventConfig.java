package com.devbelly.config;

import com.devbelly.PriceDownEventListener;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadPoolExecutor;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "eventbus.thread")
public class EventConfig {

    private String threadNamePrefix;
    private int corePoolSize;
    private int maxPoolSize;
    private int queueCapacity;

    @Bean(name = "eventbus-executor")
    public TaskExecutor eventTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix(threadNamePrefix);
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);
        executor.afterPropertiesSet();
        return executor;
    }


    @Bean
    public EventBus eventBus(TaskExecutor eventTaskExecutor) {
        return new AsyncEventBus(eventTaskExecutor);
    }

    @Bean(destroyMethod = "close")
    public PriceDownEventListener priceDownEventListener(EventBus eventBus, @Qualifier("kafkaPriceDownTemplate") KafkaTemplate kafkaTemplate){
        return new PriceDownEventListener(eventBus,kafkaTemplate);
    }
}
