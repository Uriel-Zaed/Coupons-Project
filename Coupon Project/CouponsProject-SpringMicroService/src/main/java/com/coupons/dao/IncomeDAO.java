package com.coupons.dao;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coupons.entities.Income;
import com.coupons.repository.IncomeRepository;

@Service
public class IncomeDAO {

	@Autowired
	private IncomeRepository repo;
	
	public void storeIncome(Income income) {
		repo.save(income);
	}
	
	public Collection<Income> viewAllIncome() {
		return (Collection<Income>)repo.findAll();
	}
	
	public Collection<Income> viewIncomeByCompany(String name) {
		return (Collection<Income>)repo.viewIncomeByCompany(name);
	}
	
	public Collection<Income> viewIncomeByCustomer(String name) {
		return (Collection<Income>)repo.viewIncomeByCustomer(name);
	}
}
