package com.create_builds.app.authcontroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jwt.JWTClaimsSet;

import java.net.URL;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.List;
import java.util.Map;

public class JwtVerifier {

    private static final String GOOGLE_JWKS_URI = "https://www.googleapis.com/oauth2/v3/certs";

    public static boolean verifyToken(String token) {
        try {
            // Parse the JWT
            SignedJWT signedJWT = SignedJWT.parse(token);

            // Extract the header to get the Key ID (kid)
            JWSHeader header = signedJWT.getHeader();
            String keyId = header.getKeyID();

            if (keyId == null || keyId.isEmpty()) {
                System.out.println("Key ID (kid) not found in token header.");
                return false;
            }

            // Get the public key from Google's JWKs
            RSAPublicKey publicKey = getPublicKeyFromGoogleJwks(keyId);

            if (publicKey == null) {
                System.out.println("Public key not found for keyId: " + keyId);
                return false;
            }

            // Verify the token signature
            RSASSAVerifier verifier = new RSASSAVerifier(publicKey);
            if (!signedJWT.verify(verifier)) {
                System.out.println("Invalid token signature.");
                return false;
            }

            // Validate claims (optional)
            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
            String issuer = claims.getIssuer();
            if (!"https://accounts.google.com".equals(issuer) && !"accounts.google.com".equals(issuer)) {
                System.out.println("Invalid issuer: " + issuer);
                return false;
            }

            // Optionally, you can add more claim validations here (e.g., expiration, audience)

            System.out.println("Token is valid!");
            return true;

        } catch (Exception e) {
            System.out.println("Error verifying token: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private static RSAPublicKey getPublicKeyFromGoogleJwks(String keyId) throws Exception {
        // Fetch the JWKS from Google's endpoint
        URL url = new URL(GOOGLE_JWKS_URI);
        Map<String, Object> jwks = new ObjectMapper().readValue(url, Map.class);

        List<Map<String, Object>> keys = (List<Map<String, Object>>) jwks.get("keys");
        if (keys == null) {
            throw new IllegalArgumentException("No keys found in JWKS.");
        }

        for (Map<String, Object> key : keys) {
            if (keyId.equals(key.get("kid"))) {
                // Get the X.509 certificate chain
                List<String> x5cList = (List<String>) key.get("x5c");
                if (x5cList == null || x5cList.isEmpty()) {
                    throw new IllegalArgumentException("x5c certificate chain is missing for keyId: " + keyId);
                }

                String x5c = x5cList.get(0);
                // Decode the certificate
                byte[] certBytes = Base64.getDecoder().decode(x5c);
                CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
                X509Certificate certificate = (X509Certificate) certFactory.generateCertificate(new java.io.ByteArrayInputStream(certBytes));

                // Extract the public key from the certificate
                return (RSAPublicKey) certificate.getPublicKey();
            }
        }

        throw new IllegalArgumentException("Public key not found for keyId: " + keyId);
    }
}
