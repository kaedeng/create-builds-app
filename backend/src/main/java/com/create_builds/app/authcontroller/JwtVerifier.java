package com.create_builds.app.authcontroller;

import java.net.URI;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.UrlJwkProvider;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.create_builds.app.tables.user.model.UserModel;
import com.create_builds.app.tables.user.modelservice.UserRepoService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;

@Controller
@NoArgsConstructor
public class JwtVerifier {
	
	@Autowired
	private UserRepoService userRepo;
    private static String clientId;
    private static RSAPrivateKey privateKey;
    private static RSAPublicKey publicMeKey;
    
    static {
    	try {
	    	clientId = System.getenv("GOOGLE_ID");
	    	String privateKeyPem = System.getenv("PRIVATE_RSA_KEY");
	    	String publicKeyPem = "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEA56vBpeKN4R836GaJF0MlVXbay6wpuyXo4WFc6lNoZ/G4xH4j+0tzTbs1xzfOap2sncc1LzumwMpLMDu8aExE/6qYzIZPlvPcgDMlko/NdXG+qI9vVLhfvdoPoCfbPtlH+i9hr8y8yMhck4KjxcFawzWm0LKyStnv9EAN5EnHYoyA2NqARVkj2eQ9fmPcnBoBZ0UcVaYSj/tftWIAr6Hm3tCGpjar3eAOl9i9QF4MrP8le/3Tq4kYmw+TEDaiK7DNttBzZ4owpGmxHaUF0CQcJexqVIUarYIjdL6Qj6J8tBJpiBReWHiU5E/yLzSWVgVjLiq8ipGF8EBJ/Abj/yBqA+TRDhifHEq4bCKsdK7xgx38zI2WP8+SFvj7yoWa8xN2P33luAyQXmH1ewFYZ5VRgNCMGEVB1G0ziuCzoiQ8LCl0ZL74/IXTGuYes5kj80IoE9LZ5Qaoz9+72mhtH3FmjgTCKot1b+MU1eZ6X/z4Hmb1AaxsUaH0zco5apIqiligLQDf/jDg0aU3I1rwlNWfYK4hOfFHnI1uEr7Gh3YwYYlU6uLoDCRtdxWwlZWsj4MxPRuQyuUq1J+qzpjPHJ7nvLwuq7kPDypg2xnEAYjMdiJFAIFREJn5cR5ax5ol6Pzuxy3jLqEh9hh7bOaua/YYuQEaM9wrcgk/lTE+EwNFlTcCAwEAAQ==";
	        if (clientId == null) {
	            throw new RuntimeException("Environment variables GOOGLE_ID and PRIVATE_RSA_KEY must be set");
	        }
	        
	        privateKeyPem = privateKeyPem
	                .replaceAll("\\s", "");
	        System.out.println("Cleaned Private Key: " + privateKeyPem);
	        byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyPem);
	        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyPem);
	        PKCS8EncodedKeySpec privSpec = new PKCS8EncodedKeySpec(privateKeyBytes);
	        PKCS8EncodedKeySpec pubSpec = new PKCS8EncodedKeySpec(publicKeyBytes);
	        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
	        privateKey = (RSAPrivateKey) keyFactory.generatePrivate(privSpec);
	        publicMeKey = (RSAPublicKey) keyFactory.generatePublic(pubSpec);
    	} catch (Exception e) {
            throw new RuntimeException("Failed to load private key", e);
        }
    }
    
    public Boolean verifyToken(String token, HttpServletResponse response) {
    	
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
            
            if(userRepo.getByGoogleId(decodedJWT.getSubject()) == null) {
            	UserModel newModel = new UserModel();
            	newModel.setGoogle_id(decodedJWT.getSubject());
            	newModel.setUsername(decodedJWT.getClaim("preferred_username").asString());
            	userRepo.saveModel(newModel);
            }
            
            algorithm = Algorithm.RSA256(publicMeKey, privateKey);
            String userJwt = JWT.create()
                    .withIssuer("https://createbuildsmc.com/")
                    .withSubject(userRepo.getByGoogleId(decodedJWT.getSubject()).getId().toString())
                    .withAudience(clientId)
                    .withIssuedAt(new Date())
                    .withExpiresAt(new Date(System.currentTimeMillis() + 3600 * 1000))
                    .sign(algorithm);
            
            Cookie jwtCookie = new Cookie("auth_token", userJwt);

            // Secure the cookie
            jwtCookie.setHttpOnly(true);  // Prevent access via JavaScript
            jwtCookie.setSecure(true);    // Ensure it's sent over HTTPS only
            jwtCookie.setPath("/");       // Cookie is accessible across the entire app
            jwtCookie.setMaxAge(3600);    // Cookie expires in 1 hour (same as JWT expiry)

            // Add the cookie to the response
            response.addCookie(jwtCookie);
            
            return true;
        } catch (Exception e) {
        	e.printStackTrace();
            return false;
        }
    }
}


