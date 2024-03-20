package com.coupon.beans;

import java.util.ArrayList;

public class Customer {

	private long id;
	private String custName;
	private String password;
	private ArrayList<Coupon> coupons;

	public Customer() {
		this.coupons = new ArrayList<Coupon>();
	}

	public Customer(long id, String custName, String password) {
		this.id = id;
		this.custName = custName;
		this.password = password;
		this.coupons = new ArrayList<Coupon>();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public ArrayList<Coupon> getCoupons() {
		return coupons;
	}

	public void setCoupons(ArrayList<Coupon> coupons) {
		this.coupons = coupons;
	}

	@Override
	public String toString() {
		return "Customer [id=" + id + ", custName=" + custName + ", password=" + password + ", coupons=" + coupons
				+ "]";

	}

}
