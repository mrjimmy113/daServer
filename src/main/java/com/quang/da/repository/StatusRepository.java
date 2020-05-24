package com.quang.da.repository;

import org.springframework.data.repository.CrudRepository;

import com.quang.da.entity.Status;
import com.quang.da.enumaration.StatusEnum;

public interface StatusRepository extends CrudRepository<Status, Integer> {

	Status findOneByStatus(StatusEnum status);
}
