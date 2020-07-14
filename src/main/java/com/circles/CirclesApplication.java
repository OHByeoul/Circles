package com.circles;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
public class CirclesApplication {

	public static void main(String[] args) {
		SpringApplication.run(CirclesApplication.class, args);
	}

}
