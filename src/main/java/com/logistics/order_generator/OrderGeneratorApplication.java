package com.logistics.order_generator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class OrderGeneratorApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderGeneratorApplication.class, args);
	}

}
