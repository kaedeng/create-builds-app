package com.create_builds.app.authcontroller;

import java.io.InputStream;
import java.util.Properties;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

public class JwtVerifier {

    private static String clientId;
    private static String clientSecret;
    
    static {
        try (InputStream input = JwtVerifier.class.getClassLoader().getResourceAsStream("application.properties")) {
        	
            Properties prop = new Properties();
            if (input == null) {
                throw new RuntimeException("Unable to find application.properties");
            }
            prop.load(input);
            clientId = prop.getProperty("spring.security.oauth2.client.registration.google.client-id");
            clientSecret = prop.getProperty("spring.security.oauth2.client.registration.google.client-secret");
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to load client ID from properties file", e);
        }
    }

    public static Boolean verifyToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(clientSecret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withAudience(clientId)
                    .build();
            DecodedJWT decodedJWT = verifier.verify(token);
            
            System.out.println("Token is valid.");
            System.out.println("Subject: " + decodedJWT.getSubject());
            System.out.println("Issuer: " + decodedJWT.getIssuer());
            System.out.println("Expires at: " + decodedJWT.getExpiresAt());
        } catch (Exception e) {
        	System.out.println("Token is invalid.");
            return false;
        }
        return true;
    }
}


