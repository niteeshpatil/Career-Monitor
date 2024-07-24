package com.example.Career.Monitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class CareerMonitorApplication {

	public static void main(String[] args) {
		SpringApplication.run(CareerMonitorApplication.class, args);
	}

}
