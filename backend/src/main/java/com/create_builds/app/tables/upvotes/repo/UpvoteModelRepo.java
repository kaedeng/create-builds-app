package com.create_builds.app.tables.upvotes.repo;

import org.springframework.data.repository.CrudRepository;
import com.create_builds.app.tables.upvotes.model.UpvoteModel;
import org.springframework.stereotype.Repository;
@Repository
public interface UpvoteModelRepo extends CrudRepository<UpvoteModel, Integer>{
}
