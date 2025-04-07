package com.logistics.order_generator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.h2.tools.Server;

import java.sql.SQLException;

@EnableScheduling
@SpringBootApplication
public class OrderReceiverApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderReceiverApplication.class, args);
	}

	@Bean(initMethod = "start", destroyMethod = "stop")
	public Server H2Database() throws SQLException {
		return Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9099");
	}
}
