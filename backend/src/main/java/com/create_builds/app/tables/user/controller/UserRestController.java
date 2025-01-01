package com.create_builds.app.tables.user.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.create_builds.app.baserestcontroller.BaseRestController;
import com.create_builds.app.tables.user.model.UserModel;
import com.create_builds.app.tables.user.modelservice.UserRepoService;
import com.create_builds.app.tables.user.repo.UserModelRepo;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "https://createbuildsmc.com/")
public class UserRestController extends BaseRestController<
        UserModel, 
        Integer, 
        UserModelRepo, 
        UserRepoService> {

    @Autowired
    public UserRestController(UserRepoService service) {
        super();
        this.modelrepo=service;
    }
}