package com.wizlab.mqtt.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class TaskExecutorConfigure {

    @Value("${mqtt.sub.worker.count:4}")
    private Integer mqttSubWorkerCount;

/*    @Bean(name="mqttSubExecutor")
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setThreadNamePrefix("MQTT_SUB_WOKER");
        taskExecutor.setCorePoolSize(4);	      // 기본 스레드 수
        taskExecutor.setMaxPoolSize(16);	      // 최대 스레드 수
        taskExecutor.setQueueCapacity(40000);	  // Queue 사이즈

        return taskExecutor;
    }*/

    @Bean(name="mqttSubExecutorService")
    public ExecutorService mqttSubExecutorService() {
        return Executors.newFixedThreadPool(this.mqttSubWorkerCount,
                new CustomizableThreadFactory("mqttSubWorker-thread-"));
    }
}
