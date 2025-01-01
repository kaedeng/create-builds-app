package com.create_builds.app.tables.upvotes.repo;

import org.springframework.data.repository.CrudRepository;
import com.create_builds.app.tables.upvotes.model.UpvoteModel;

public interface UpvoteModelRepo extends CrudRepository<UpvoteModel, Integer>{
}
