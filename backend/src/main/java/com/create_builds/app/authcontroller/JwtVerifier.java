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
    	clientId = System.getenv("GOOGLE_ID");
        clientSecret = System.getenv("GOOGLE_SECRET_ID");

        if (clientId == null || clientSecret == null) {
            throw new RuntimeException("Environment variables GOOGLE_ID and GOOGLE_SECRET_ID must be set");
        }
    }

    public static Boolean verifyToken(String token) {
    	
    	// DEBUG LINES
    	
    	System.out.println("clientId: " + clientId);
    	System.out.println("clientSecret: " + clientSecret);
    	System.out.println("token: " + token);
    	
        if (token == null || token.length() <= 2) {
            throw new IllegalArgumentException("Token is too short to strip characters");
        }
        String strippedToken = token.substring(1, token.length() - 1);
    	
    	System.out.println("Stripped Token: " + strippedToken);
    	
    	// CODE
    	
        try {
            Algorithm algorithm = Algorithm.HMAC256(clientSecret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withAudience(clientId)
                    .build();
            DecodedJWT decodedJWT = verifier.verify(strippedToken);
            
            System.out.println("Token is valid.");
            System.out.println("Subject: " + decodedJWT.getSubject());
            System.out.println("Issuer: " + decodedJWT.getIssuer());
            System.out.println("Expires at: " + decodedJWT.getExpiresAt());
        } catch (Exception e) {
        	System.out.println("Token is invalid. Stack Trace:");
        	e.printStackTrace();
            return false;
        }
        return true;
    }
}


