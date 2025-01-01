package com.create_builds.app.tables.user.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import com.create_builds.app.tables.user.model.UserModel;
import org.springframework.stereotype.Repository;
@Repository
public interface UserModelRepo extends JpaRepository<UserModel, Integer>{
}
