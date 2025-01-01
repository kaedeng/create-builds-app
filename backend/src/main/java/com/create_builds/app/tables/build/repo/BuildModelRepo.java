package com.create_builds.app.tables.build.repo;

import org.springframework.data.repository.CrudRepository;
import com.create_builds.app.tables.build.model.BuildModel;
import org.springframework.stereotype.Repository;
@Repository
public interface BuildModelRepo extends CrudRepository<BuildModel, Integer>{
}