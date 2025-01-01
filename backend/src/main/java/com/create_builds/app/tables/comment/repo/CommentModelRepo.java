package com.create_builds.app.tables.comment.repo;

import org.springframework.data.repository.CrudRepository;
import com.create_builds.app.tables.comment.model.CommentModel;

public interface CommentModelRepo extends CrudRepository<CommentModel, Integer>{
}
