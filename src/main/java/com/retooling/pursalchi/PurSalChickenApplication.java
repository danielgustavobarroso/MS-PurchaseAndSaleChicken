package com.retooling.pursalchi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;

@SpringBootApplication
@EnableDiscoveryClient
@OpenAPIDefinition(servers = {@Server(url = "http://localhost:8011/ms-pursalchicken")})
public class PurSalChickenApplication {
	
	private static final Logger logger = LoggerFactory.getLogger(PurSalChickenApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(PurSalChickenApplication.class, args);
		logger.info("Iniciando PurSalChickenApplication...");
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate(); 
	}
	
}
