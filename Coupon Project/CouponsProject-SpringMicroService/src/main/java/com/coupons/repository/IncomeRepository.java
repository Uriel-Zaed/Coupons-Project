package com.coupons.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.coupons.entities.Income;

public interface IncomeRepository extends CrudRepository<Income, Long> {

	@Query("SELECT i FROM Income i WHERE i.name = :name AND i.userType='COMPANY'")
	Iterable<Income> viewIncomeByCompany(@Param("name")String name);

	@Query("SELECT i FROM Income i WHERE i.name = :name  AND i.userType='CUSTOMER'")
	Iterable<Income> viewIncomeByCustomer(@Param("name")String name);

	
}

