	package com.create_builds.app.tables.upvotes.repo;
	
	import org.springframework.data.jpa.repository.JpaRepository;
	import org.springframework.data.jpa.repository.Query;
	import com.create_builds.app.tables.upvotes.model.UpvoteModel;
	import org.springframework.stereotype.Repository;
	@Repository
	public interface UpvoteModelRepo extends JpaRepository<UpvoteModel, Integer>{
		@Query("SELECT c FROM UpvoteModel c WHERE c.build_id = :buildId")
		public UpvoteModel findUpvoteByBuildId(Integer buildId);
	}
