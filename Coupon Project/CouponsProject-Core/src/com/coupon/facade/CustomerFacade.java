package com.coupon.facade;

import java.util.Collection;

import com.coupon.beans.Coupon;
import com.coupon.beans.CouponType;
import com.coupon.beans.Customer;
import com.coupon.couponDAO.CouponDAO;
import com.coupon.couponDAO.CouponDBDAO;
import com.coupon.couponDAO.CustomerDAO;
import com.coupon.couponDAO.CustomerDBDAO;
import com.coupon.exception.MyException;



public class CustomerFacade implements CouponClientFacade {

	private CustomerDAO customerDAO = null;
	private CouponDAO couponDAO =null ; 
	private Customer currentCustomer;
	
	public CustomerFacade() {
		this.customerDAO = new CustomerDBDAO();
		this.couponDAO = new CouponDBDAO();
		
		
	}
	
	@Override
	public CouponClientFacade login(String name, String password, UserType userType) throws MyException {
		if(customerDAO.login(name, password)) {
			System.out.println("login customer seccessed!");
			this.currentCustomer = customerDAO.getCustomerByName(name);
			return this;
		}
		System.out.println("login customer failed");

		return null;
	}
	
	public void purchaseCoupon (Coupon coupon) throws MyException
	{
		try {
			couponDAO.getCoupon(coupon.getId());
		} catch (MyException e) {
			throw new MyException ("there is a problem eith the database.");
		}
		customerDAO.customerPurchaseCoupon(coupon, currentCustomer);
	}
	
	
	public Collection<Coupon> getAllPurchasedCoupons() throws MyException {
		
			return customerDAO.getAllCoupons(currentCustomer.getId());
			
		
	}
	
	
	public Collection<Coupon> getAllPurchasedCouponsByType(CouponType couponType) throws MyException {
		
		return customerDAO.getCouponsByType(currentCustomer.getId() ,couponType);
	}
	
	public Collection<Coupon> getAllPurchasedCouponsByPrice(double price) throws MyException {
		
		return customerDAO.getCouponsByPrice(currentCustomer.getId() ,price);
	}
	
	public Customer getCustomerInfo() {
		return this.currentCustomer;
	}
	
	
	
	

}
