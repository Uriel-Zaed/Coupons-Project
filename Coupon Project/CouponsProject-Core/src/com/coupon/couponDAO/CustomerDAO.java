package com.coupon.couponDAO;

import java.util.ArrayList;
import java.util.Collection;

import com.coupon.beans.Coupon;
import com.coupon.beans.CouponType;
import com.coupon.beans.Customer;
import com.coupon.exception.MyException;

public interface CustomerDAO {

	
	public void createCustomer(Customer customer) throws MyException;
	public void removeCustomer(Customer customer) throws MyException;
	public void updateCustomer(Customer customer) throws MyException;
	public void customerPurchaseCoupon(Coupon coupon, Customer customer) throws MyException ;

	
	public Customer getCustomer(int id) throws  MyException;
	public ArrayList<Customer> getAllCustomer() throws MyException;
	public boolean login (String custName, String password) throws MyException;
	ArrayList<Coupon> getAllCoupons(long id) throws MyException;
	public Collection<Coupon> getCouponsByType(long id, CouponType couponType) throws MyException;
	public Collection<Coupon> getCouponsByPrice(long id, double price) throws MyException;
	Customer getCustomerByName(String name) throws MyException;
	
	
}
