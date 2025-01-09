package com.create_builds.app.tables.comment.controller;

import java.util.List;

import com.create_builds.app.authcontroller.CookieVerifier;
import com.create_builds.app.tables.build.model.BuildModel;
import com.create_builds.app.tables.build.modelservice.BuildRepoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CookieValue;

import com.create_builds.app.baserestcontroller.BaseRestController;
import com.create_builds.app.tables.comment.model.CommentModel;
import com.create_builds.app.tables.comment.modelservice.CommentRepoService;
import com.create_builds.app.tables.comment.repo.CommentModelRepo;

@RestController
@RequestMapping("/api/builds/{buildId}/comments")
@CrossOrigin(origins = "https://createbuildsmc.com",
			 allowCredentials = "true")
public class CommentRestController{

    @Autowired
    CommentRepoService modelrepo;

    @Autowired
    CookieVerifier cookieVerifier;
	
    public List<CommentModel> getComments(Integer buildId) {
        return modelrepo.findCommentsByBuildId(buildId);
    }
    
    @GetMapping
    public ResponseEntity<List<CommentModel>> fetchByBuildId(@PathVariable("buildId") Integer buildId) {
        try {
            List<CommentModel> entity = getComments(buildId);

            if(entity.isEmpty()) return ResponseEntity.noContent().build();

            return ResponseEntity.ok(entity);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping
    public ResponseEntity<CommentModel> postComment(@CookieValue(name = "auth_token", required = true) String authToken, @PathVariable("buildId") Integer buildId, @RequestBody CommentModel commentModel){
        try {
            Integer user_id = cookieVerifier.CookieVerifierAndIntExtractor(authToken);
            if(user_id.equals(-1)) throw new RuntimeException("Invalid token or user ID failed");

            commentModel.setBuild_id(buildId);
            commentModel.setUser_id(user_id);

            modelrepo.saveModel(commentModel);

            return ResponseEntity.ok(commentModel);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentModel> putComment(@CookieValue(name = "auth_token", required = true) String authToken, @PathVariable("buildId") Integer buildId, @PathVariable Integer id, @RequestBody CommentModel commentModel){
        try {
            Integer user_id = cookieVerifier.CookieVerifierAndIntExtractor(authToken);
            if(user_id.equals(-1)) throw new RuntimeException("Invalid token or user ID failed");

            CommentModel savedModel = modelrepo.getModelById(id);

            if(!(savedModel.getUser_id()).equals(user_id)) throw new RuntimeException("Build's owner doesn't match");
            if(!(savedModel.getBuild_id()).equals(buildId)) throw new RuntimeException("Build's id doesn't match");

            savedModel.setContent(commentModel.getContent());

            modelrepo.updateModel(savedModel, id);

            return ResponseEntity.ok(savedModel);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteComment(@CookieValue(name = "auth_token", required = true) String authToken, @PathVariable("buildId") Integer buildId, @PathVariable Integer id){
        try {
            Integer user_id = cookieVerifier.CookieVerifierAndIntExtractor(authToken);
            if(user_id.equals(-1)) throw new RuntimeException("Invalid token or user ID failed");

            CommentModel commentModel = modelrepo.getModelById(id);

            if(commentModel == null) throw new RuntimeException("Comment doesn't exist");

            if(!(commentModel.getUser_id()).equals(user_id)) throw new RuntimeException("Build's owner doesn't match");
            if(!(commentModel.getBuild_id()).equals(buildId)) throw new RuntimeException("Build's id doesn't match");

            modelrepo.delModel(id);

            return ResponseEntity.ok("Comment Deleted");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
