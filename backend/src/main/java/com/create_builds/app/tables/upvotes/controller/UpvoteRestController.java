package com.create_builds.app.tables.upvotes.controller;

import java.util.List;

import com.create_builds.app.authcontroller.CookieVerifier;
import com.create_builds.app.reposervice.RepoService;
import com.create_builds.app.tables.build.model.BuildModel;
import com.create_builds.app.tables.build.modelservice.BuildRepoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CookieValue;

import com.create_builds.app.baserestcontroller.BaseRestController;
import com.create_builds.app.tables.upvotes.model.UpvoteModel;
import com.create_builds.app.tables.upvotes.modelservice.UpvoteRepoService;
import com.create_builds.app.tables.upvotes.repo.UpvoteModelRepo;

@RestController
@RequestMapping("/api/{buildId}/upvote")
@CrossOrigin(origins = "https://createbuildsmc.com",
			 allowCredentials = "true")
public class UpvoteRestController{

	@Autowired
	private BuildRepoService buildmodelrepo;

	@Autowired
	UpvoteRepoService modelrepo;

	@Autowired
	CookieVerifier cookieVerifier;
  	
  	public void deleteByBuildId(Integer buildId, Integer userId) {
  	    UpvoteModel toDel = modelrepo.getUpvoteByBuildAndUser(buildId, userId);
  	    if (toDel == null) {
  	        throw new IllegalArgumentException("No upvote found for buildId: " + buildId);
  	    }

		Integer upvotes = buildmodelrepo.getModelById(buildId).getUpvotes();
		buildmodelrepo.getModelById(buildId).setUpvotes(--upvotes);

  	    modelrepo.delModel(toDel.getId());
  	}

	public void postByBuildId(Integer buildId, Integer userId) {

		if (modelrepo.getUpvoteByBuildAndUser(buildId, userId) != null) {
			throw new IllegalArgumentException("Upvote already exists for buildId: " + buildId);
		}

		UpvoteModel toPost = new UpvoteModel();

		toPost.setUser_id(userId);
		toPost.setBuild_id(buildId);

		Integer upvotes = buildmodelrepo.getModelById(buildId).getUpvotes();
		buildmodelrepo.getModelById(buildId).setUpvotes(++upvotes);

		modelrepo.saveModel(toPost);
	}

  	@DeleteMapping
    public ResponseEntity<String> deleteEntity(@CookieValue(name = "auth_token", required = true) String authToken, @PathVariable Integer buildId) {
		try {
			Integer user_id = cookieVerifier.CookieVerifierAndIntExtractor(authToken);
			if(user_id.equals(-1)) throw new RuntimeException("Invalid token or user ID failed");

			deleteByBuildId(buildId, user_id);

			return ResponseEntity.ok("Upvote Deleted");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
    }

	@PostMapping
	public ResponseEntity<String> postUpvote(@CookieValue(name = "auth_token", required = true) String authToken, @PathVariable Integer buildId){
		try {
			Integer user_id = cookieVerifier.CookieVerifierAndIntExtractor(authToken);
			if(user_id.equals(-1)) throw new RuntimeException("Invalid token or user ID failed");

			postByBuildId(buildId, user_id);

			return ResponseEntity.ok("Upvote Created");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}