package com.logistics.order_generator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class OrderReceiverApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderReceiverApplication.class, args);
	}

}
