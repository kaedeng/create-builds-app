package com.create_builds.app.authcontroller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "https://createbuildsmc.com",
			 allowCredentials = "true")
public class AuthController {

	@PostMapping("/api/login")
	public String authorizeLogin(@RequestBody String idToken) {
		System.out.println("Received token: " + idToken);
		boolean isValid = JwtVerifier.verifyToken(idToken);
        System.out.println("Is token valid? " + isValid);
		return "Win?" + idToken;
	}
	
    @GetMapping("/api/health/ping")
    public String ping() {
        System.out.println("pinged!");
        return "pong";
    }
}
