package com.coupons.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.coupons.dao.IncomeDAO;
import com.coupons.entities.Income;
import com.coupons.utilities.ApplicationResponse;

@Controller
@RequestMapping(path="IncomeService")
public class MainController {

	@Autowired
	private IncomeDAO dao;
	
	@PostMapping(path="income")
	public @ResponseBody Object storeIncome(@RequestBody Income income) {
		dao.storeIncome(income);
		return new ApplicationResponse(0, "Income added successfully.");
	}
	
	@GetMapping(path="income")
	public @ResponseBody Object viewAllIncome() {
		return dao.viewAllIncome();
	}
	
	@GetMapping(path="income/company")
	public @ResponseBody Object viewIncomeByCompany(@RequestParam(required=true) String name) {
		return dao.viewIncomeByCompany(name);
	}
	@GetMapping(path="income/customer")
	public @ResponseBody Object viewIncomeByCustomer(@RequestParam(required=true) String name) {
		return dao.viewIncomeByCustomer(name);
	}
	
}
