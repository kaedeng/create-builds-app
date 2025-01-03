package com.create_builds.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
public class CreateBuildsAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(CreateBuildsAppApplication.class, args);
	}

}
