package com.create_builds.app.authcontroller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

	@GetMapping("/api/login")
	public String authorizeLogin() {
		return "Hi";
	}
    @GetMapping("/api/health/ping")
    public String ping() {
        System.out.println("pinged!");
        return "pong";
    }
}
