package com.create_builds.app.authcontroller;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
	import org.springframework.security.web.SecurityFilterChain;

	@org.springframework.context.annotation.Configuration
	public class SecurityConfig {

	    @Bean
	    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	        http
	            .authorizeHttpRequests(auth -> auth
	            	.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
	            	.requestMatchers("/api/login").permitAll()
	                .anyRequest().permitAll() 
	            )
	            .oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()))
		        .csrf(csrf -> csrf.disable())
		        .cors(Customizer.withDefaults())
	            .requiresChannel(channel -> 
	            channel.anyRequest().requiresSecure());
	        return http.build();
	    }
	}

