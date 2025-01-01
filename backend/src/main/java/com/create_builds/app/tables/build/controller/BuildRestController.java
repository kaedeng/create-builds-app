package com.create_builds.app.tables.build.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.create_builds.app.tables.build.model.BuildModel;
import com.create_builds.app.tables.build.modelservice.BuildRepoService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;



@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "https://createbuildsmc.com")
public class BuildRestController {
	@Autowired
	BuildRepoService modelrepo;
    
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
    
    @PostMapping("/builds")
    public BuildModel postBuild(@RequestBody BuildModel model) {
        return modelrepo.saveModel(model);
    }
    
    @PutMapping("/builds/{id}")
    public BuildModel putBuild(@PathVariable Integer id, @RequestBody BuildModel model) {
    	return modelrepo.updateModel(model, id);
    }
    
    @DeleteMapping("/builds/{id}")
    public void deleteBuild(@PathVariable Integer id) {
    	modelrepo.delModel(id);
    }
    
}
