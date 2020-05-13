package com.quang.da.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.quang.da.entity.Customer;

public interface CustomerRepository extends CrudRepository<Customer, Integer>{
	Optional<Customer> findOneByEmailAndPassword(String email, String password);
	Optional<Customer> findOneByEmail(String email);
}
