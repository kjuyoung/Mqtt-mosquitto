package com.wizlab.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan(basePackages = "com.wizlab.common")
@SpringBootApplication(scanBasePackages = {"com.wizlab.api", "com.wizlab.common.domain"})
@EnableJpaRepositories(basePackages = "com.wizlab.common.repository")
public class ApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}
}
