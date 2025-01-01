package com.create_builds.app.tables.build.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.create_builds.app.tables.build.model.BuildModel;
import com.create_builds.app.tables.comment.model.CommentModel;

import java.util.List;

@Repository
public interface BuildModelRepo extends JpaRepository<BuildModel, Integer> {
	@Query("SELECT b FROM BuildModel b ORDER BY b.upvotes DESC")
    public List<BuildModel> findTopBuilds();
	@Query("SELECT b FROM BuildModel b WHERE b.user_id = :userId")
    public List<BuildModel> findBuildsByUserId(Integer userId);
}
