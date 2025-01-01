package com.create_builds.app.tables.comment.repo;

import org.springframework.data.repository.CrudRepository;
import com.create_builds.app.tables.comment.model.CommentModel;
import org.springframework.stereotype.Repository;
@Repository
public interface CommentModelRepo extends CrudRepository<CommentModel, Integer>{
}
