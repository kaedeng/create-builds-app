package com.create_builds.app.authcontroller;

//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//import org.springframework.web.filter.ForwardedHeaderFilter;
//import org.springframework.web.cors.CorsConfigurationSource;
//
//import java.util.List;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//	@Bean
//	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//	    http
//	            .cors(Customizer.withDefaults())
//	            .authorizeHttpRequests(auth -> auth
//	                            .requestMatchers(HttpMethod.GET, "/", "/api/homepage-builds", "/api/builds/**", "/api/health/ping").permitAll()
//	                            .anyRequest().permitAll()
//	            )
//	            .oauth2Login(oauth2Login -> 
//	            oauth2Login
//	                .successHandler((request, response, authentication) -> {
//	                    OAuth2User user = (OAuth2User) authentication.getPrincipal();
//	                    System.out.println("User details: " + user.getAttributes());
//	                    response.sendRedirect("https://createbuildsmc.com");
//	                })
//	                .failureHandler((request, response, exception) -> {
//	                    exception.printStackTrace();
//	                })
//	            )
//	            
//	    return http.build();
//	}
//}

	
	import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
	import org.springframework.security.web.SecurityFilterChain;

	@org.springframework.context.annotation.Configuration
	public class SecurityConfig {

	    @Bean
	    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	        http
	            .authorizeHttpRequests(auth -> auth
	                .anyRequest().authenticated() // All requests require authentication
	            )
	            .oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()))
		        .csrf(csrf -> csrf.disable())
	            .requiresChannel(channel -> 
	            channel.anyRequest().requiresSecure());
	        return http.build();
	    }
	}

