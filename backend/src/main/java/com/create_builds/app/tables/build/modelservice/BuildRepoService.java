package com.create_builds.app.tables.build.modelservice;

import org.springframework.stereotype.Service;
import com.create_builds.app.reposervice.RepoService;
import com.create_builds.app.tables.build.model.BuildModel;
import com.create_builds.app.tables.build.repo.BuildModelRepo;

@Service
public class BuildRepoService extends RepoService<BuildModel, Integer, BuildModelRepo>{
	public BuildRepoService(BuildModelRepo modelrepo){
		super();
        this.modelrepo = modelrepo;
	}
	
	@Override
	public BuildModel updateModel(BuildModel model, Integer id) {
		BuildModel upd = getModelById(id);
		
		if (model.getUser_id() != null) {
	        upd.setUser_id(model.getUser_id());
	    }
	    if (model.getTitle() != null && !model.getTitle().isEmpty()) {
	        upd.setTitle(model.getTitle());
	    }
	    if (model.getDescription() != null && !model.getDescription().isEmpty()) {
	        upd.setDescription(model.getDescription());
	    }
	    if (model.getImg_links() != null && model.getImg_links().length > 0) {
	        upd.setImg_links(model.getImg_links());
	    }
	    if (model.getNbt() != null && !model.getNbt().isEmpty()) {
	        upd.setNbt(model.getNbt());
	    }
	    if (model.getUpvotes() != null) {
	        upd.setUpvotes(model.getUpvotes());
	    }
	    return modelrepo.save(upd);
	}
}
