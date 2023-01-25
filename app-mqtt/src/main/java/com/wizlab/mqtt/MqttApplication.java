package com.wizlab.mqtt;

import com.wizlab.mqtt.config.MqttConfig;
import com.wizlab.mqtt.service.MqttSubService;
import com.wizlab.mqtt.service.MqttSubWorker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;

@EntityScan(basePackages = "com.wizlab.common.domain")
@SpringBootApplication(scanBasePackages = {"com.wizlab.mqtt", "com.wizlab.common.domain"})
@EnableJpaRepositories(basePackages = "com.wizlab.common.repository")
// @SpringBootApplication
public class MqttApplication {

	public static void main(String[] args) {
//		SpringApplication.run(TestMqttSubApplication.class, args);

		BlockingQueue<String> mqttSubQueue = new LinkedBlockingQueue<>();

		ConfigurableApplicationContext context =  SpringApplication.run(MqttApplication.class, args);

		// MQTT Subscribe WorkerThread
		ExecutorService mqttSubExecutorService = (ExecutorService) context.getBean("mqttSubExecutorService");

		// MQTT Sub Worker Thread 생성
		MqttSubService mqttSubService = (MqttSubService) context.getBean("mqttSubService");
		int workerCount = 1;

		for (int i = 0; i < workerCount; i++) {
			MqttSubWorker worker = MqttSubWorker.builder()
							.queue(mqttSubQueue)
							.mqttSubService(mqttSubService)
							.build();

			mqttSubExecutorService.submit(worker);
		}

		// MQTT Subscribe EventHandler 에 별도의 Queue를 Attach
		MqttConfig mqttSubMsgHandler = (MqttConfig) context.getBean("mqttConfig");
		mqttSubMsgHandler.attachMqttSubQueue(mqttSubQueue);

		// MQTT Subscribe WorkerThread Graceful Shutdown
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				mqttSubExecutorService.shutdownNow();
			}
		});
	}
}
