package com.create_builds.app.tables.upvotes.modelservice;

import org.springframework.stereotype.Service;

import com.create_builds.app.reposervice.RepoService;
import com.create_builds.app.tables.upvotes.model.UpvoteModel;
import com.create_builds.app.tables.upvotes.repo.UpvoteModelRepo;

@Service
public class UpvoteRepoService extends RepoService<UpvoteModel, Integer, UpvoteModelRepo>{

	public UpvoteRepoService(UpvoteModelRepo modelrepo) {
        super();
        this.modelrepo = modelrepo;
    }
	
	@Override
	public UpvoteModel updateModel(UpvoteModel model, Integer id) {
		UpvoteModel upd = getModelById(id);
		
        if (model.getBuild_id() != null) {
            upd.setBuild_id(model.getBuild_id());
        }
        if (model.getUser_id() != null) {
            upd.setUser_id(model.getUser_id());
        }
        if (model.getUpvote_count() != null) {
            upd.setUpvote_count(model.getUpvote_count());
        }
		return modelrepo.save(upd);
	}
}
