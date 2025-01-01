package com.create_builds.app.tables.user.repo;

import org.springframework.data.repository.CrudRepository;
import com.create_builds.app.tables.user.model.UserModel;

public interface UserModelRepo extends CrudRepository<UserModel, Integer>{
}
