package com.create_builds.app.tables.user.controller;

import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.create_builds.app.tables.user.model.UserModel;
import com.create_builds.app.tables.user.modelservice.UserRepoService;


@RestController
@RequestMapping("/api/profile")
@CrossOrigin(origins = "https://createbuildsmc.com",
			 allowCredentials = "true")
public class UserRestController {

    @Autowired
    UserRepoService modelrepo;
    
    public UserModel getUser(Integer id) {
        return modelrepo.getModelById(id);
    }
    
    public void delUser(@CookieValue Integer id) {
    	modelrepo.delModel(id);
    }
    
    @GetMapping
    public ResponseEntity<UserModel> fetch(@CookieValue Integer id) {
        try {
        	UserModel entity = getUser(id);
            return ResponseEntity.ok(entity);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEntity(@CookieValue Integer id) {
        try {
            delUser(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}