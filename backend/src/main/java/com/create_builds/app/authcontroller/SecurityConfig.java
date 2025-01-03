package com.create_builds.app.authcontroller;

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

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    private final ClientRegistrationRepository clientRegistrationRepository;

    public SecurityConfig(ClientRegistrationRepository clientRegistrationRepository) {
        this.clientRegistrationRepository = clientRegistrationRepository;
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            )
            .oauth2Login(oauth2 -> oauth2
                .authorizationEndpoint(authEndpoint -> authEndpoint
                    // Set the custom resolver here
                    .authorizationRequestResolver(customAuthorizationRequestResolver())
                )
                // We'll add a success handler next
                .successHandler(oauth2AuthenticationSuccessHandler())
            );
        
        return http.build();
    }

    @Bean
    OAuth2AuthorizationRequestResolver customAuthorizationRequestResolver() {
        // Spring’s default path to trigger OAuth2 login: /oauth2/authorization/{registrationId}
        DefaultOAuth2AuthorizationRequestResolver defaultResolver =
            new DefaultOAuth2AuthorizationRequestResolver(clientRegistrationRepository, "/oauth2/authorization");

        return new OAuth2AuthorizationRequestResolver() {
            @Override
            public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
                // This method is invoked if you go to /oauth2/authorization/google
                OAuth2AuthorizationRequest req = defaultResolver.resolve(request);
                return customizeAuthorizationRequest(req, request);
            }

            @Override
            public OAuth2AuthorizationRequest resolve(HttpServletRequest request, String clientRegistrationId) {
                // This method is invoked if you go to /oauth2/authorization/{clientRegistrationId}
                OAuth2AuthorizationRequest req = defaultResolver.resolve(request, clientRegistrationId);
                return customizeAuthorizationRequest(req, request);
            }

            private OAuth2AuthorizationRequest customizeAuthorizationRequest(
                    OAuth2AuthorizationRequest req, HttpServletRequest request) {
                if (req == null) {
                    return null;
                }
                // Grab the `state` param from the request
                String stateParam = request.getParameter("state");
                if (stateParam != null) {
                    // Put it into additionalParameters so it ends up in the real OAuth2 request
                    Map<String, Object> extraParams = new HashMap<>(req.getAdditionalParameters());
                    extraParams.put("state", stateParam);

                    return OAuth2AuthorizationRequest.from(req)
                            .additionalParameters(extraParams)
                            .build();
                }
                return req;
            }
        };
    }

    @Bean
    AuthenticationSuccessHandler oauth2AuthenticationSuccessHandler() {
        return (request, response, authentication) -> {
            // After successful login, we can retrieve the OAuth2AuthorizedClient or the current OAuth2AuthenticationToken
            // We also want to retrieve the `state` if it's still available.

            // 1) One approach: read the state from OAuth2AuthenticationToken or from saved request attributes.
            //    Spring keeps "state" in the OAuth2AuthorizationRequest when exchanging the code.
            //    Some data might be in the SecurityContext or the HttpSession.

            // For a simple approach, if all you need is to redirect back to the subdomain:

            String stateParam = request.getParameter("state");
            if (stateParam != null && !stateParam.isEmpty()) {
                // e.g. redirect the user to "https://sub.myapp.com/whatever"
                response.sendRedirect("https://" + stateParam + "/post-login-page");
            } else {
                // fallback if no state
                response.sendRedirect("/");
            }
        };
    }
}


