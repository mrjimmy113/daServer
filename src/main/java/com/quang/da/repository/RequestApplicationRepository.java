package com.quang.da.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.quang.da.entity.Expert;
import com.quang.da.entity.ProblemRequest;
import com.quang.da.entity.RequestApplication;

@Repository
public interface RequestApplicationRepository extends CrudRepository<RequestApplication, Integer> {
	
	@Query("SELECT r.problemRequest.id FROM RequestApplication as r WHERE r.expert.id = :expertId ")
	List<Integer> findAppliedRequestIds(@Param("expertId") int expertId);
	
	@Query("SELECT r.expert FROM RequestApplication as r WHERE r.problemRequest.requestId = :requestId ")
	List<Expert> findAllExpertByRequestId(@Param("requestId") int requestId);
	
	@Query("SELECT r.problemRequest FROM RequestApplication as r WHERE r.expert.id = :expertId ")
	List<ProblemRequest> findAppliedRequests(@Param("expertId") int expertId);
	
	
	@Modifying
	@Query("DELETE FROM RequestApplication as r WHERE r.problemRequest.requestId = :requestId")
	void deleteApplicationByRequestId(@Param("requestId") int requestId);
}
