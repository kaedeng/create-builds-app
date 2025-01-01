package com.create_builds.app.tables.upvotes.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.create_builds.app.baserestcontroller.BaseRestController;
import com.create_builds.app.tables.upvotes.model.UpvoteModel;
import com.create_builds.app.tables.upvotes.modelservice.UpvoteRepoService;
import com.create_builds.app.tables.upvotes.repo.UpvoteModelRepo;

@RestController
@RequestMapping("/api/upvotes")
@CrossOrigin(origins = "https://createbuildsmc.com/")
public class UpvoteRestController extends BaseRestController<
        UpvoteModel, 
        Integer, 
        UpvoteModelRepo, 
        UpvoteRepoService> {

    public UpvoteRestController(UpvoteRepoService service) {
        super();
        this.modelrepo=service;
    }
}