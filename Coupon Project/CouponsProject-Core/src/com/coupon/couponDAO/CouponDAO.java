package com.coupon.couponDAO;

import java.util.ArrayList;

import com.coupon.beans.Coupon;
import com.coupon.beans.CouponType;
import com.coupon.exception.MyException;

public interface CouponDAO {
	
	public void createCoupon (Coupon coupon) throws MyException;
	public void removeCoupon(Coupon coupon) throws MyException;
	public void updateCoupon(Coupon coupon) throws MyException;
	
	public Coupon getCoupon (long id) throws  MyException;
	public ArrayList<Coupon> getAllCoupon() throws MyException;
	public ArrayList<Coupon> getCouponByType(CouponType couponType) throws MyException;
	
	

		
	
	

}
