package com.create_builds.app.tables.comment.controller;

import org.springframework.web.bind.annotation.*;

import com.create_builds.app.baserestcontroller.BaseRestController;
import com.create_builds.app.tables.comment.model.CommentModel;
import com.create_builds.app.tables.comment.modelservice.CommentRepoService;
import com.create_builds.app.tables.comment.repo.CommentModelRepo;

@RestController
@RequestMapping("/api/comments")
@CrossOrigin(origins = "https://createbuildsmc.com/")
public class CommentRestController extends BaseRestController<
        CommentModel, 
        Integer, 
        CommentModelRepo, 
        CommentRepoService> {
	
    public CommentRestController(CommentRepoService service) {
        super();
        this.modelrepo = service;
    }
}
