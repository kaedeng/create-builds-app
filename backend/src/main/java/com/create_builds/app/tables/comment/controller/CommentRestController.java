package com.create_builds.app.tables.comment.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.create_builds.app.baserestcontroller.BaseRestController;
import com.create_builds.app.tables.comment.model.CommentModel;
import com.create_builds.app.tables.comment.modelservice.CommentRepoService;
import com.create_builds.app.tables.comment.repo.CommentModelRepo;

@RestController
@RequestMapping("/api/builds/{buildId}/comments")
@CrossOrigin(origins = "https://createbuildsmc.com")
public class CommentRestController extends BaseRestController<
	CommentModel, 
	Integer, 
	CommentModelRepo, 
	CommentRepoService>
	{
	public CommentRestController(CommentRepoService service) {
        super();
        this.modelrepo=service;
    }

	// nullifying bad protocol
	@Override
	protected List<CommentModel> getAll() {
        return null;
    }
	
    public List<CommentModel> getComments(Integer buildId) {
        return modelrepo.findCommentsByBuildId(buildId);
    }
    
    @GetMapping
    public ResponseEntity<List<CommentModel>> fetchByBuildId(@PathVariable("buildId") Integer buildId) {
        try {
            List<CommentModel> entity = getComments(buildId);
            return ResponseEntity.ok(entity);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

}
