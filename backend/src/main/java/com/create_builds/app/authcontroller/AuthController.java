package com.create_builds.app.authcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.create_builds.app.tables.user.modelservice.UserRepoService;
import com.create_builds.app.tables.user.repo.UserModelRepo;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@CrossOrigin(origins = "https://createbuildsmc.com",
			 allowCredentials = "true")
public class AuthController {

	@Autowired
	UserRepoService userRepoService;
	
	@PostMapping("/api/login")
	public String authorizeLogin(@RequestBody String idToken, HttpServletResponse response) {
		
		JwtVerifier bruh = new JwtVerifier(userRepoService);
		Boolean isValid = bruh.verifyToken(idToken, response);
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
