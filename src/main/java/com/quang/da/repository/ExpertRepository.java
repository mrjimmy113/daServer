package com.quang.da.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.quang.da.entity.Expert;

public interface ExpertRepository extends CrudRepository<Expert, Integer> {
	Optional<Expert> findOneByEmailAndPassword(String email, String password);
	Optional<Expert> findOneByEmail(String email);
}
