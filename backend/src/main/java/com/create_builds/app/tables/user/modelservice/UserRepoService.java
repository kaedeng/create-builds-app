package com.create_builds.app.tables.user.modelservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.create_builds.app.reposervice.RepoService;
import com.create_builds.app.tables.comment.repo.CommentModelRepo;
import com.create_builds.app.tables.user.model.UserModel;
import com.create_builds.app.tables.user.repo.UserModelRepo;

@Service
public class UserRepoService extends RepoService<UserModel, Integer, UserModelRepo>{
	@Autowired
	public UserRepoService(UserModelRepo modelrepo){
		super();
        this.modelrepo = modelrepo;
	}
	@Override
	public UserModel updateModel(UserModel model, Integer id) {
		UserModel upd = getModelById(id);
		
		if (model.getGoogle_id() != null) {
	        upd.setGoogle_id(model.getGoogle_id());
	    }
	    if (model.getUsername() != null && !model.getUsername().isEmpty()) {
	        upd.setUsername(model.getUsername());
	    }
		return modelrepo.save(upd);
	}

}
