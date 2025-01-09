package com.create_builds.app.tables.comment.modelservice;

import java.util.List;

import org.springframework.stereotype.Service;

import com.create_builds.app.reposervice.RepoService;
import com.create_builds.app.tables.comment.model.CommentModel;
import com.create_builds.app.tables.comment.repo.CommentModelRepo;

@Service
public class CommentRepoService extends RepoService<CommentModel, Integer, CommentModelRepo>{
	public CommentRepoService(CommentModelRepo modelrepo){
		super();
        this.modelrepo = modelrepo;
	}
	
	@Override
	public CommentModel updateModel(CommentModel model, Integer id) {
        CommentModel upd = getModelById(id);

        if (model.getBuild_id() != null) {
            upd.setBuild_id(model.getBuild_id());
        }
        if (model.getUser_id() != null) {
            upd.setUser_id(model.getUser_id());
        }
        if (model.getContent() != null && !model.getContent().isEmpty()) {
            upd.setContent(model.getContent());
        }
        return modelrepo.save(upd);
	}

	public List<CommentModel> findCommentsByBuildId(Integer buildId) {
		return modelrepo.findCommentsByBuildId(buildId);
	}
}


