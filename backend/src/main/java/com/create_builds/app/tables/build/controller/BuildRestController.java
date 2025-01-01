package com.create_builds.app.tables.build.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.create_builds.app.baserestcontroller.BaseRestController;
import com.create_builds.app.tables.build.model.BuildModel;
import com.create_builds.app.tables.build.modelservice.BuildRepoService;
import com.create_builds.app.tables.build.repo.BuildModelRepo;

@RestController
@RequestMapping("/api/builds")
@CrossOrigin(origins = "https://createbuildsmc.com/")
public class BuildRestController extends BaseRestController<
        BuildModel, 
        Integer, 
        BuildModelRepo, 
        BuildRepoService> {

    public BuildRestController(BuildRepoService service) {
        super();
        this.modelrepo = service;
    }
}
