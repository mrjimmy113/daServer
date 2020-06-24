package com.quang.da.repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.quang.da.entity.Customer;
import com.quang.da.entity.Expert;
import com.quang.da.entity.ProblemRequest;
import com.quang.da.enumaration.StatusEnum;

public interface ProblemRequestRepository extends CrudRepository<ProblemRequest, Integer> {

	
	@Query("SELECT r FROM ProblemRequest r WHERE r.id = :id  AND r.customer.id = :customerId")
	Optional<ProblemRequest> findOneByIdAndCustomerId(@Param("id") int id, @Param("customerId") int customerId);
	
	@Query("SELECT r FROM ProblemRequest r WHERE r.expert.id = :id")
	List<ProblemRequest> findByExpertId(@Param("id") int id);
	
	@Query("SELECT r FROM ProblemRequest r WHERE r.customer.id = :id")
	List<ProblemRequest> findByCustomerId(@Param("id") int id);
	
	@Query("SELECT r FROM ProblemRequest r WHERE r.expert.id = :id AND r.status.status IN :status")
	List<ProblemRequest> findByExpertIdAndStatus(@Param("id") int id, @Param("status") StatusEnum[] statusEnum, Pageable pageable);
	
	@Query("SELECT r FROM ProblemRequest r WHERE r.customer.id = :id AND r.status.status IN :status")
	List<ProblemRequest> findByCustomerIdAndStatus(@Param("id") int id, @Param("status") StatusEnum[] statusEnum, Pageable pageable);
	
	@Query("SELECT r FROM ProblemRequest r WHERE "
			+ "r.major.id = :majorId AND "
			+ "r.customer.city = :city AND "
			+ "r.customer.primaryLanguage = :language AND "
			+ "r.status.status = 'NEW' AND "
			+ "r.createdDate BETWEEN :startDate AND :endDate")
	List<ProblemRequest> findByMajorCityLanguageStartDateEndDate(
			@Param("majorId") int id, @Param("city") String city,
			@Param("language") String language,
			@Param("startDate") Date startDate,
			@Param("endDate") Date endDate);
	
	@Query("SELECT r FROM ProblemRequest r WHERE "
			+ "r.major.id = :majorId AND "
			+ "r.customer.city = :city AND "
			+ "r.status.status = 'NEW' AND "
			+ "r.createdDate BETWEEN :startDate AND :endDate")
	List<ProblemRequest> findByMajorCityStartDateEndDate(
			@Param("majorId") int id, @Param("city") String city,
			@Param("startDate") Date startDate,
			@Param("endDate") Date endDate);
	
	@Query("SELECT r FROM ProblemRequest r WHERE "
			+ "r.major.id = :majorId AND "
			+ "r.customer.primaryLanguage = :language AND "
			+ "r.status.status = 'NEW' AND "
			+ "r.createdDate BETWEEN :startDate AND :endDate")
	List<ProblemRequest> findByMajorLanguageStartDateEndDate(
			@Param("majorId") int id,
			@Param("language") String language,
			@Param("startDate") Date startDate,
			@Param("endDate") Date endDate);
	
	@Query("SELECT r FROM ProblemRequest r WHERE "
			+ "r.major.id = :majorId AND "
			+ "r.id NOT IN :appliedList AND "
			+ "r.status.status = 'NEW' AND "
			+ "r.createdDate BETWEEN :startDate AND :endDate")
	List<ProblemRequest> findByMajorStartDateEndDate(
			@Param("majorId") int id,
			@Param("appliedList") List<Integer> appliedList,
			@Param("startDate") Date startDate,
			@Param("endDate") Date endDate);
	
	@Query("SELECT r.customer FROM ProblemRequest r WHERE r.requestId = :id")
	Optional<Customer> findCustomerInRequest(@Param("id") int requestId);
	
	@Query("SELECT r.expert FROM ProblemRequest r WHERE r.requestId = :id")
	Optional<Expert> findExpertInRequest(@Param("id") int requestId);
	
	@Query("SELECT r.requestId FROM ProblemRequest r WHERE r.customer.id = :id "
			+ "AND r.status.status IN ('ACCEPTED', 'PROCESSING','TMPCOMPLETE','TMPCANCEL')")
	List<Number> customerFindSubableRequest(@Param("id") int id);
	
	@Query("SELECT r.requestId FROM ProblemRequest r WHERE r.expert.id = :id "
			+ "AND r.status.status IN ('ACCEPTED', 'PROCESSING','TMPCOMPLETE','TMPCANCEL')")
	List<Number> expertFindSubableRequest(@Param("id") int id);
}
