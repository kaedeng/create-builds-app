package com.create_builds.app.tables.user.controller;

import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.create_builds.app.authcontroller.CookieVerifier;
import com.create_builds.app.authcontroller.GoogleJwtVerifier;
import com.create_builds.app.tables.user.model.UserModel;
import com.create_builds.app.tables.user.modelservice.UserRepoService;


@RestController
@RequestMapping("/api/profile")
@CrossOrigin(origins = "https://createbuildsmc.com",
			 allowCredentials = "true")
public class UserRestController {

    @Autowired
    UserRepoService modelrepo;
    
    @Autowired
	CookieVerifier cookieVerifier;
    
    public UserModel getUser(Integer id) {
        return modelrepo.getModelById(id);
    }
    
    public void delUser(Integer id) {
    	modelrepo.delModel(id);
    }
    
    public UserModel updateUser(UserModel userModel, Integer id) {
    	return modelrepo.updateModel(userModel, id);
    }
    
    @GetMapping
    public ResponseEntity<UserModel> fetch(@CookieValue(name = "auth_token") String cookie) {
        try {
        	Integer id = cookieVerifier.CookieVerifierAndIntExtractor(cookie);
        	if(id.equals(-1)) throw new RuntimeException("Invalid token or user ID failed");
        	UserModel entity = getUser(id);
            return ResponseEntity.ok(entity);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    
    @PutMapping
    public ResponseEntity<UserModel> updateEntity(@CookieValue(name = "auth_token") String cookie, @RequestBody UserModel userModel) {
        try {
        	Integer id = cookieVerifier.CookieVerifierAndIntExtractor(cookie);
        	if(id.equals(-1)) throw new RuntimeException("Invalid token or user ID failed");
            UserModel entity = updateUser(userModel, id);
            return ResponseEntity.ok(entity);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    
    @DeleteMapping
    public ResponseEntity<Void> deleteEntity(@CookieValue(name = "auth_token") String cookie) {
        try {
        	Integer id = cookieVerifier.CookieVerifierAndIntExtractor(cookie);
        	if(id.equals(-1)) throw new RuntimeException("Invalid token or user ID failed");
            delUser(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    
}