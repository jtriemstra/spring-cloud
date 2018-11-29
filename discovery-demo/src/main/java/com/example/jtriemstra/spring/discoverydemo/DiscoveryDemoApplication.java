package com.example.jtriemstra.spring.discoverydemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class DiscoveryDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DiscoveryDemoApplication.class, args);
	}
}
