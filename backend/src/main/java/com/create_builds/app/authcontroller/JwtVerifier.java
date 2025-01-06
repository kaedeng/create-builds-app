package com.create_builds.app.authcontroller;

import java.net.URI;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

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

@Service
public class JwtVerifier {
	
	private UserRepoService userRepo;
    private static String clientId;
    private static RSAPrivateKey privateKey;
    private static RSAPublicKey publicMeKey;
    
    public JwtVerifier(UserRepoService userRepo) {
        try {
        	this.userRepo = userRepo;
            clientId = System.getenv("GOOGLE_ID");
            String privateKeyPem = System.getenv("PRIVATE_RSA_KEY");
            String publicKeyPem = "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEA1KHeK3LrUbhM5J3k1UEhCW/TpzD93fM6rahNCGzLFqanmxVrVQzSkWDoFJ5RZBa1UDk6FSJR3u27q//ddYC3qigtbAlV/nVs2WMt+YKAOTM42w+enFqg9XM2uoIFIgjbTCuYxnYtqtXjhGUxvUU88i6Yzw+8/VQFtzIbmWqpmlBqmerXZAlsZkNp6QVtVxL5nTXRGje+W0BXoaFsLNwJaM96aid/CctTRGyDmOumK6ys1q/rOTBrLpVKDJP6AjjxFXseH4qSBUXvrPYoptOl2LYazvJHryRZCv9ZK2rBesvd6Tw9RWPgMuIjUMRflmn/A8cuGqN+JILJCligtqpIJcL0jg4dY/lOV7bZ3V6aFtgjkEmQr/OOTDthLtgKSYAcEDMy+P+PasM+VtTh/GtKfBA7E/GGe6LXd4gC/dlc29Vnd7a6pyXcglQIUR57zAm7WQuN3JiQeACYQ5QZW8aQQRMX5yvSwR3c0QSFEMR6wPa8QLyP5j+0/UhYuyWbPBBYJd7R6sWflJltST/M5V+OFYSIbla1/C+StB9PpZYRF6x18fUv6oU+JKXBm+XMC8EQj7hGFtp0E35TaoWCvHbpUdnclVmWejNLenZeksW2CnB9ZjHJHfwEFw7v3umgsBNktHWLyqEo8sMU7Y/gn6id7dr+Zo3YxCrWTDAP2uKooQsCAwEAAQ==";

            if (clientId == null) {
                throw new RuntimeException("Environment variables GOOGLE_ID and PRIVATE_RSA_KEY must be set");
            }

            privateKeyPem = privateKeyPem.replaceAll("\\s", "");
            byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyPem);
            byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyPem);

            PKCS8EncodedKeySpec privSpec = new PKCS8EncodedKeySpec(privateKeyBytes);
            X509EncodedKeySpec pubSpec = new X509EncodedKeySpec(publicKeyBytes);

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            privateKey = (RSAPrivateKey) keyFactory.generatePrivate(privSpec);
            publicMeKey = (RSAPublicKey) keyFactory.generatePublic(pubSpec);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load keys", e);
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
            
            algorithm = Algorithm.RSA256(null, privateKey);
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


