package com.create_builds.app.authcontroller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
		http
			.authorizeHttpRequests(auth -> auth
					.requestMatchers(HttpMethod.GET, "/", "/homepage-builds", "/builds/**", "/health/ping").permitAll()
					.anyRequest().authenticated())
			.oauth2Login(Customizer.withDefaults())
			.requiresChannel(channel -> channel.anyRequest().requiresSecure());
		return http.build();
	}
	
}
