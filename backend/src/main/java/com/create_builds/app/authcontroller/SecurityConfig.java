//package com.create_builds.app.authcontroller;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@org.springframework.context.annotation.Configuration
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//            .authorizeHttpRequests(auth -> auth
//                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//                .requestMatchers("/api/login").permitAll()
//                .anyRequest().permitAll()
//            )
//            .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
//            .csrf(csrf -> csrf.disable())
//            .cors(Customizer.withDefaults())
//            .requiresChannel(channel -> channel.anyRequest().requiresSecure())
//            .oauth2Login(oauth2 -> oauth2
//                .successHandler((HttpServletRequest request, HttpServletResponse response, Authentication authentication) -> {
//                    System.out.println("Authentication successful for user: " + authentication.getName());
//                    response.getWriter().write("Authentication Successful!");
//                    response.getWriter().flush();
//                })
//            );
//        return http.build();
//    }
//}
