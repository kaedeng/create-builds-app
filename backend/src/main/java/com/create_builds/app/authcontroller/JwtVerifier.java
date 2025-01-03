package com.create_builds.app.authcontroller;

import java.net.URI;
import java.security.interfaces.RSAPublicKey;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.UrlJwkProvider;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

public class JwtVerifier {

    private static String clientId;
    
    static {
    	clientId = System.getenv("GOOGLE_ID");

        if (clientId == null) {
            throw new RuntimeException("Environment variables GOOGLE_ID  must be set");
        }
    }
    
    public static Boolean verifyToken(String token) {
    	
        String strippedToken = token.substring(1, token.length() - 1);

        try {
        	UrlJwkProvider provider = new UrlJwkProvider(new URI("https://www.googleapis.com/oauth2/v3/certs").normalize().toURL());
        	
        	DecodedJWT jwt = JWT.decode(strippedToken);

        	Jwk jwk = provider.get(jwt.getKeyId());
        	RSAPublicKey publicKey = (RSAPublicKey) jwk.getPublicKey();
            Algorithm algorithm = Algorithm.RSA256(publicKey, null);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withAudience(clientId)
                    .build();
            DecodedJWT decodedJWT = verifier.verify(strippedToken);
            
            System.out.println("Token is valid.");
            System.out.println("Subject: " + decodedJWT.getSubject());
            System.out.println("Issuer: " + decodedJWT.getIssuer());
            System.out.println("Expires at: " + decodedJWT.getExpiresAt());
            return true;
        } catch (Exception e) {
        	System.out.println("Token is invalid. Stack Trace:");
        	e.printStackTrace();
            return false;
        }
    }
}


