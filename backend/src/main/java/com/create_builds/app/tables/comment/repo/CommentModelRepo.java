package com.create_builds.app.tables.comment.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.create_builds.app.tables.comment.model.CommentModel;
import org.springframework.stereotype.Repository;
@Repository
public interface CommentModelRepo extends JpaRepository<CommentModel, Integer>{
	@Query("SELECT c FROM CommentModel c WHERE c.build_id = :buildId")
	public List<CommentModel> findCommentsByBuildId(Integer id);
}
