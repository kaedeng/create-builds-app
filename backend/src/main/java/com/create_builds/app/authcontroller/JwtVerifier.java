package com.create_builds.app.authcontroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jwt.JWTClaimsSet;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;
import java.util.List;
import java.util.Map;

public class JwtVerifier {

    private static final String GOOGLE_JWKS_URI = "https://www.googleapis.com/oauth2/v3/certs";
    private static final String JWKS_CACHE_FILE = "jwks_cache.json";

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

            System.out.println("Token is valid!");
            return true;

        } catch (Exception e) {
            System.out.println("Error verifying token: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private static RSAPublicKey getPublicKeyFromGoogleJwks(String keyId) throws Exception {
        try {
            // Fetch the JWKS from Google's endpoint
            Map<String, Object> jwks = fetchJwksFromUrl(GOOGLE_JWKS_URI);

            List<Map<String, Object>> keys = (List<Map<String, Object>>) jwks.get("keys");
            if (keys == null) {
                throw new IllegalArgumentException("No keys found in JWKS.");
            }

            for (Map<String, Object> key : keys) {
                if (keyId.equals(key.get("kid"))) {
                    return extractPublicKeyFromJwk(key);
                }
            }
        } catch (IOException e) {
            System.out.println("Failed to fetch JWKS from URL, attempting to load cached JWKS.");
            return getPublicKeyFromCachedJwks(keyId);
        }

        throw new IllegalArgumentException("Public key not found for keyId: " + keyId);
    }

    private static Map<String, Object> fetchJwksFromUrl(String jwksUrl) throws IOException {
        URL url = new URL(jwksUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(5000); // 5 seconds timeout
        connection.setReadTimeout(5000);    // 5 seconds timeout

        try (InputStream inputStream = connection.getInputStream()) {
            saveJwksToCache(inputStream); // Save to cache
            return new ObjectMapper().readValue(new File(JWKS_CACHE_FILE), Map.class);
        }
    }

    private static void saveJwksToCache(InputStream jwksStream) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(JWKS_CACHE_FILE)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = jwksStream.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
        }
    }

    private static RSAPublicKey getPublicKeyFromCachedJwks(String keyId) throws Exception {
        File cacheFile = new File(JWKS_CACHE_FILE);
        if (!cacheFile.exists()) {
            throw new IllegalStateException("JWKS cache not found.");
        }

        Map<String, Object> jwks = new ObjectMapper().readValue(cacheFile, Map.class);
        List<Map<String, Object>> keys = (List<Map<String, Object>>) jwks.get("keys");

        for (Map<String, Object> key : keys) {
            if (keyId.equals(key.get("kid"))) {
                return extractPublicKeyFromJwk(key);
            }
        }

        throw new IllegalArgumentException("Public key not found for keyId: " + keyId);
    }

    private static RSAPublicKey extractPublicKeyFromJwk(Map<String, Object> key) throws Exception {
        List<String> x5cList = (List<String>) key.get("x5c");
        if (x5cList == null || x5cList.isEmpty()) {
            throw new IllegalArgumentException("x5c certificate chain is missing for key.");
        }

        String x5c = x5cList.get(0);
        byte[] certBytes = Base64.getDecoder().decode(x5c);
        CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
        X509Certificate certificate = (X509Certificate) certFactory.generateCertificate(new ByteArrayInputStream(certBytes));

        return (RSAPublicKey) certificate.getPublicKey();
    }
}
