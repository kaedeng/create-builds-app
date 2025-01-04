package com.create_builds.app.authcontroller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "https://createbuildsmc.com",
			 allowCredentials = "true")
public class AuthController {

	@GetMapping("/api/login")
	public String authorizeLogin() {
		return "Win?";
	}
    @GetMapping("/api/health/ping")
    public String ping() {
        System.out.println("pinged!");
        return "pong";
    }
}
