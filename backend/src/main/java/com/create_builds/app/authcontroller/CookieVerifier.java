package com.create_builds.app.authcontroller;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Base64;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

@Service
public class CookieVerifier {

	private static String clientId;
	private static RSAPublicKey publicKey;

	public CookieVerifier() {
		try {
			
			clientId = System.getenv("GOOGLE_ID");
			
			String publicKeyPem = "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEA1KHeK3LrUbhM5J3k1UEhCW/TpzD93fM6rahNCGzLFqanmxVrVQzSkWDoFJ5RZBa1UDk6FSJR3u27q//ddYC3qigtbAlV/nVs2WMt+YKAOTM42w+enFqg9XM2uoIFIgjbTCuYxnYtqtXjhGUxvUU88i6Yzw+8/VQFtzIbmWqpmlBqmerXZAlsZkNp6QVtVxL5nTXRGje+W0BXoaFsLNwJaM96aid/CctTRGyDmOumK6ys1q/rOTBrLpVKDJP6AjjxFXseH4qSBUXvrPYoptOl2LYazvJHryRZCv9ZK2rBesvd6Tw9RWPgMuIjUMRflmn/A8cuGqN+JILJCligtqpIJcL0jg4dY/lOV7bZ3V6aFtgjkEmQr/OOTDthLtgKSYAcEDMy+P+PasM+VtTh/GtKfBA7E/GGe6LXd4gC/dlc29Vnd7a6pyXcglQIUR57zAm7WQuN3JiQeACYQ5QZW8aQQRMX5yvSwR3c0QSFEMR6wPa8QLyP5j+0/UhYuyWbPBBYJd7R6sWflJltST/M5V+OFYSIbla1/C+StB9PpZYRF6x18fUv6oU+JKXBm+XMC8EQj7hGFtp0E35TaoWCvHbpUdnclVmWejNLenZeksW2CnB9ZjHJHfwEFw7v3umgsBNktHWLyqEo8sMU7Y/gn6id7dr+Zo3YxCrWTDAP2uKooQsCAwEAAQ==";
			
			byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyPem);
			X509EncodedKeySpec pubSpec = new X509EncodedKeySpec(publicKeyBytes);
			
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			publicKey = (RSAPublicKey) keyFactory.generatePublic(pubSpec);
			
			if (clientId == null) {
                throw new RuntimeException("Environment variables GOOGLE_ID and PRIVATE_RSA_KEY must be set");
            }
			
		} catch (Exception e) {
			throw new RuntimeException("Failed to load keys", e);
		}
	}
	
	public Integer CookieVerifierAndIntExtractor(String cookie) {
		
		String[] crumblCookie = cookie.split("\\.");
		
		try {
			
			if(crumblCookie.length != 3) return -1;
			
			String payload = new String(Base64.getUrlDecoder().decode(crumblCookie[1]));
			String signature = crumblCookie[2];
			
			Algorithm algorithm = Algorithm.RSA256(publicKey, null);
			
			JWTVerifier verifier = JWT.require(algorithm)
					.withIssuer("https://createbuildsmc.com/")
					.withAudience(clientId)
					.build();
			DecodedJWT decodedJWT = verifier.verify(cookie);
			
			ObjectMapper objectMapper = new ObjectMapper();
			Map<String, Object> objectMap = objectMapper.readValue(payload, Map.class);
			
			return Integer.parseInt((objectMap.get("sub").toString()));
			
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
		
	}
	
}
