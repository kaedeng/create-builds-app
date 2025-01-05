package com.create_builds.app.authcontroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jwt.JWTClaimsSet;

import java.net.URL;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.List;
import java.util.Map;

public class JwtVerifier {

    private static final String GOOGLE_JWKS_URI = "https://www.googleapis.com/oauth2/v3/certs";

    public static boolean verifyToken(String token) {
        try {
            // Decode the JWT
            SignedJWT signedJWT = SignedJWT.parse(token);

            // Fetch Google's public keys (JWKs)
            JWSObject jwsObject = JWSObject.parse(token);
            Map<String, Object> header = jwsObject.getHeader().toJSONObject();
            String keyId = (String) header.get("kid");

            // Get the public key from Google's JWKs
            RSAPublicKey publicKey = getPublicKeyFromGoogleJwks(keyId);

            // Verify the token signature
            RSASSAVerifier verifier = new RSASSAVerifier(publicKey);
            if (!signedJWT.verify(verifier)) {
                System.out.println("Invalid token signature");
                return false;
            }

            // Validate claims (optional)
            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
            String issuer = claims.getIssuer();
            if (!"https://accounts.google.com".equals(issuer)) {
                System.out.println("Invalid issuer");
                return false;
            }

            System.out.println("Token is valid!");
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static RSAPublicKey getPublicKeyFromGoogleJwks(String keyId) throws Exception {
        URL url = new URL(GOOGLE_JWKS_URI);
        Map<String, Object> jwks = new ObjectMapper().readValue(url, Map.class);

        for (Map<String, Object> key : (List<Map<String, Object>>) jwks.get("keys")) {
            if (keyId.equals(key.get("kid"))) {
                // Cast x5c to List and get the first certificate
                List<String> x5cList = (List<String>) key.get("x5c");
                String publicKeyPem = x5cList.get(0);

                byte[] encoded = Base64.getDecoder().decode(publicKeyPem);
                X509EncodedKeySpec spec = new X509EncodedKeySpec(encoded);
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                return (RSAPublicKey) keyFactory.generatePublic(spec);
            }
        }

        throw new IllegalArgumentException("Public key not found for keyId: " + keyId);
    }
}
