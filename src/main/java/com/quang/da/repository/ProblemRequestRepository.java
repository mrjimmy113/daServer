package com.quang.da.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.quang.da.entity.ProblemRequest;

public interface ProblemRequestRepository extends CrudRepository<ProblemRequest, Integer> {

	
	@Query("SELECT r FROM ProblemRequest r WHERE r.id = :id  AND r.customer.id = :customerId")
	Optional<ProblemRequest> findOneByIdAndCustomerId(@Param("id") int id, @Param("customerId") int customerId);
	
	@Query("SELECT r FROM ProblemRequest r WHERE r.expert.id = :id")
	List<ProblemRequest> findByExpertId(@Param("id") int id);
	
	@Query("SELECT r FROM ProblemRequest r WHERE r.customer.id = :id")
	List<ProblemRequest> findByCustomerId(@Param("id") int id);
}
