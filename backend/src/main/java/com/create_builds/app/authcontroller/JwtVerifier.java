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
	    	String publicKeyPem = "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAACAQDSITa/FpjXyfizk2ZNm38Iu+xx55mQrQTxMc9qtQQT109i+iRQ0m2nY8WjtcTYFwMAZln6gnlo3pcBP3N9cpEyxUHNhBAo5w1a2A4qxXvVgZhzOx4h65779A0VNITbfxZs6LJvIhuZD14UH3QoZNky8fHL+zvlbjiWsjV/aqvTcV8pvaPtHHfPleO6RuRLXdeq1CkWmBX1bF76mBvng6QgM1SiV2+/BH7s+jtoEDPDrIZqxFYZr56azu/bUV94RD5E5g9vIaOYHGglMGJ+jCPAp3ZzTnfMNxH2b3xnesCbClgMVqW8+ywG6uhsZCNo0oaE0AKY/jy/QrpVV5BtLCsa3qFaKTEX5ZRaTqXNgTv2EQ575QFgK7STflw8uVGOM3cnnKICqoO/bC6o9ozygxF6RzoFPIbGqvf72ri7iwz2mcYe2g3DG8VkJRL1SGoLEp6ocr0elacLhBC8tDS6i9Nt1Yn/xUETEAhz8szeP4Q0unpvzcguxWCD4LKkrI5ViPcrpGnCxaE0MsY8idxTGLADRPGYTPIHq3/3+VuyxJx+Uig7u610LdursbWRmhVGisnJaopDQteuScdPme4rfPm3ZXmD47Z84MjkisczsovixZFaNXAOKDQJNP6OIe81sSwIyVz24xc12qiezdmL3bVyed7ASaJGYp8ZntmUvC1dnw== nicho@sprouts";
	        if (clientId == null) {
	            throw new RuntimeException("Environment variables GOOGLE_ID and PRIVATE_RSA_KEY must be set");
	        }
	        
	        privateKeyPem = privateKeyPem
	                .replace("-----BEGIN PRIVATE KEY-----", "")
	                .replace("-----END PRIVATE KEY-----", "")
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


