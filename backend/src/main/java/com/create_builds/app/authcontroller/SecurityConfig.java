package com.create_builds.app.authcontroller;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.Customizer;
import org.springframework.http.HttpMethod;

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
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .cors(Customizer.withDefaults())
//                .authorizeHttpRequests(auth -> auth
//                                .requestMatchers(HttpMethod.GET, "/", "/api/homepage-builds", "/api/builds/**", "/api/health/ping").permitAll()
//                                .requestMatchers("/api/login").authenticated()
//                                .anyRequest().authenticated()
//                )
//                .oauth2Login(oauth2Login -> 
//                oauth2Login
//	                .successHandler((request, response, authentication) -> {
//	                    OAuth2User user = (OAuth2User) authentication.getPrincipal();
//	                    System.out.println("User details: " + user.getAttributes());
//	                    response.sendRedirect("https://createbuildsmc.com");
//	                })
//                    .failureHandler((request, response, exception) -> {
//                        exception.printStackTrace();
//                    })
//                )
//                .csrf(csrf -> csrf.disable());
//        
//        return http.build();
//    }
//
//
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(List.of("https://createbuildsmc.com", "https://api.createbuildsmc.com"));
//        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//        configuration.setAllowedHeaders(List.of("*"));
//        configuration.setAllowCredentials(true);
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }
//    
//    @Bean
//    public ForwardedHeaderFilter forwardedHeaderFilter() {
//        return new ForwardedHeaderFilter();
//    }
//
//}

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
		http
		
			.authorizeHttpRequests(auth -> {
                auth.requestMatchers(HttpMethod.GET, "/", "/api/homepage-builds", "/api/builds/**", "/api/health/ping").permitAll();
                auth.anyRequest().authenticated();
			})
			.oauth2Login(oauth2 -> oauth2
	                .successHandler((request, response, authentication) -> {
	                    // After successful authentication, redirect to your frontend
	                    response.sendRedirect("https://createbuildsmc.com");
	            })
	        );
		
		return http.build();
	}
	
}

