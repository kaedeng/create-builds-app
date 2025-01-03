package com.create_builds.app.tables.upvotes.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.create_builds.app.baserestcontroller.BaseRestController;
import com.create_builds.app.tables.upvotes.model.UpvoteModel;
import com.create_builds.app.tables.upvotes.modelservice.UpvoteRepoService;
import com.create_builds.app.tables.upvotes.repo.UpvoteModelRepo;

@RestController
@RequestMapping("/api/{buildId}/upvote")
@CrossOrigin(origins = "https://createbuildsmc.com",
			 allowCredentials = "true")
public class UpvoteRestController extends BaseRestController<
        UpvoteModel, 
        Integer, 
        UpvoteModelRepo, 
        UpvoteRepoService> {

    public UpvoteRestController(UpvoteRepoService service) {
        super();
        this.modelrepo=service;
    }
    // nullifying bad protocol
 	@Override
 	protected List<UpvoteModel> getAll() {
         return null;
    }
 	// nullifying bad protocol
 	@Override
 	protected UpvoteModel update(Integer id, UpvoteModel model) {
         return null;
     }
 	// nullifying bad protocol
  	@Override
 	protected UpvoteModel getById(Integer id) {
        return null;
    }
  	// nullifying bad protocol
  	@Override
  	protected void delete(Integer id) {
  		return;
  	}
  	
  	public void deleteByBuildId(Integer buildId) {
  	    UpvoteModel toDel = modelrepo.getUpvoteByBuildId(buildId);
  	    if (toDel == null) {
  	        throw new IllegalArgumentException("No upvote found for buildId: " + buildId);
  	    }
  	    modelrepo.delModel(toDel.getId());
  	}

  	
  	@DeleteMapping
    public ResponseEntity<Void> deleteEntity(@PathVariable Integer buildId) {
        try {
            deleteByBuildId(buildId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}