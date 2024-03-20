package com.coupon.facade;

import java.util.ArrayList;

import com.coupon.beans.Company;
import com.coupon.beans.Customer;
import com.coupon.couponDAO.CompanyDAO;
import com.coupon.couponDAO.CompanyDBDAO;
import com.coupon.couponDAO.CustomerDAO;
import com.coupon.couponDAO.CustomerDBDAO;
import com.coupon.exception.MyException;


public class AdminFacade implements CouponClientFacade {

	private CompanyDAO companyDAO = null;
	private CustomerDAO customerDAO = null;
	
	public AdminFacade() {
		this.companyDAO = new CompanyDBDAO();
		this.customerDAO = new CustomerDBDAO();
	}
	
	
	/**
	 * override login method from interface CouponClientFacade which this class
	 * is implements.
	 */
	@Override
	public CouponClientFacade login(String name, String password, UserType userType) throws MyException {
		if (name.equals("admin") && password.equals("1234") && userType.equals(UserType.ADMIN)) {
			System.out.println("login admin seccessed!");
			return this;
		}
		System.out.println("login admiin failed");

		return null;
		
	}
	
	
	/**
	 * this is the company methods. Administrator can create company, remove company, 
	 * update company and get information about companies.
	
	 * @throws MyException
	 */
		public void createCompany(Company company) throws MyException {
			companyDAO.createCompany(company);
		}
		public void removeCompany(Company company) throws MyException {
			companyDAO.removeCompany(company);
		}
		public void updateCompany(Company company) throws MyException {
			companyDAO.updateCompany(company);
		}
		public Company getCompany(int id) throws MyException {
			return companyDAO.getCompany(id);
		}
		public ArrayList<Company> getAllCompanys() throws MyException {
			return companyDAO.getAllCompanies();
		}
		
		/**
		 * this is the methods administrator can do with customers. he can 
		 * create , remove and update customers, also can get info about customers.
		 
		 * @throws MyException
		 */
		public void createCustomer(Customer customer) throws MyException {
			customerDAO.createCustomer(customer);
		}
		public void removeCustomer(Customer customer) throws MyException {
			customerDAO.removeCustomer(customer);
		}
		public void updateCustomer(Customer customer) throws MyException {
			customerDAO.updateCustomer(customer);
		}
		public Customer getCustomer(int id) throws MyException {
			return customerDAO.getCustomer(id);
		}
		public ArrayList<Customer> getAllCustomers() throws MyException {
			return customerDAO.getAllCustomer();
		}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
