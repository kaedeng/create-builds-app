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
		Boolean isValid = JwtVerifier.verifyToken(idToken);
        if(isValid)
		return "JWT Token Authorized";
        else
        return "JWT Token could not be verified";
	}
	
    @GetMapping("/api/health/ping")
    public String ping() {
        System.out.println("pinged!");
        return "pong";
    }
}
