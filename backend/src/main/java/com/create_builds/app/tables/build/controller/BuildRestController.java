package com.create_builds.app.tables.build.controller;

import java.util.List;

import com.create_builds.app.tables.upvotes.model.UpvoteModel;
import com.create_builds.app.tables.upvotes.modelservice.UpvoteRepoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.create_builds.app.tables.upvotes.controller.UpvoteRestController;
import com.create_builds.app.authcontroller.CookieVerifier;
import com.create_builds.app.tables.build.model.BuildModel;
import com.create_builds.app.tables.build.modelservice.BuildRepoService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;



@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "https://createbuildsmc.com",
			 allowCredentials = "true")
public class BuildRestController {
	@Autowired
	BuildRepoService modelrepo;

	@Autowired
	private UpvoteRepoService upvotemodelrepo;
	
	@Autowired
	CookieVerifier cookieVerifier;
    
    public List<BuildModel> getAllBuilds() {
		return null;
	}
    
    @GetMapping("/builds/{id}")
    public BuildModel getBuildById(@PathVariable Integer id) {
    	return modelrepo.getModelById(id);
    }
    
    public List<BuildModel> findTopBuilds() {
        return modelrepo.findTopBuilds();
    }
    
    @GetMapping("/homepage-builds")
    public ResponseEntity<List<BuildModel>> fetchTopBuilds() {
        List<BuildModel> builds = modelrepo.findTopBuilds();
        if (builds.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content if no builds
        }
        return ResponseEntity.ok(builds); // 200 OK with data
    }
    
    @GetMapping("/profile/builds")
    public ResponseEntity<List<BuildModel>> fetchUserProfileBuilds(@CookieValue(name = "auth_token", required = true) String authToken) {
    	try {
    		Integer user_id = cookieVerifier.CookieVerifierAndIntExtractor(authToken);
        	if(user_id.equals(-1)) throw new RuntimeException("Invalid token or user ID failed");
        	
        	List<BuildModel> builds = modelrepo.findBuildsFromUserId(user_id);
        	
        	if(builds.isEmpty()) {
        		return ResponseEntity.noContent().build();
        	}
        	return ResponseEntity.ok(builds);
    	} catch (Exception e) {
        	e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PostMapping("/builds")
    public ResponseEntity<BuildModel> postBuild(@CookieValue(name = "auth_token", required = true) String authToken, @RequestBody BuildModel model) {
    	try {
    		Integer user_id = cookieVerifier.CookieVerifierAndIntExtractor(authToken);
        	if(user_id.equals(-1)) throw new RuntimeException("Invalid token or user ID failed");
        	
        	model.setUser_id(user_id);
        	model.setUpvotes(1);
        	
        	BuildModel savedModel = modelrepo.saveModel(model);

			UpvoteModel toPost = new UpvoteModel();

			toPost.setUser_id(user_id);
			toPost.setBuild_id(savedModel.getId());

			upvotemodelrepo.saveModel(toPost);
        	
        	return ResponseEntity.ok(savedModel);
    	} catch (Exception e) {
        	e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PutMapping("/builds/{id}")
    public ResponseEntity<BuildModel> putBuild(@CookieValue(name = "auth_token", required = true) String authToken, @PathVariable Integer id, @RequestBody BuildModel model) {
    	try {
    		Integer user_id = cookieVerifier.CookieVerifierAndIntExtractor(authToken);
        	if(user_id.equals(-1)) throw new RuntimeException("Invalid token or user ID failed");
        	
        	BuildModel currentModel = modelrepo.getModelById(id);
        	
        	if(!(currentModel.getUser_id()).equals(user_id)) throw new RuntimeException("Build's owner doesn't match");
     
        	BuildModel updatedModel = modelrepo.updateModel(model, id);
        	
        	return ResponseEntity.ok(updatedModel);
    	} catch (Exception e) {
        	e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @DeleteMapping("/builds/{id}")
    public ResponseEntity<String> deleteBuild(@CookieValue(name = "auth_token", required = true) String authToken, @PathVariable Integer id) {
    	try {
    		Integer user_id = cookieVerifier.CookieVerifierAndIntExtractor(authToken);
        	if(user_id.equals(-1)) throw new RuntimeException("Invalid token or user ID failed");
        	
        	BuildModel currentModel = modelrepo.getModelById(id);
        	
        	if(!(currentModel.getUser_id()).equals(user_id)) throw new RuntimeException("Build's owner doesn't match");
     
        	modelrepo.delModel(id);
        	
        	return ResponseEntity.ok("Build Deleted");
    	} catch (Exception e) {
        	e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    	
    }
    
}
