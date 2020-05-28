package com.quang.da.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.quang.da.entity.Major;

@Repository
public interface MajorRepository extends JpaRepository<Major, Integer> {

}
