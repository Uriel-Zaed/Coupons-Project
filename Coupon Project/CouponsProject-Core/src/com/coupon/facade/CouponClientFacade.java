package com.coupon.facade;

import com.coupon.exception.MyException;

public interface CouponClientFacade {
	
	public CouponClientFacade login(String name, String password, UserType userType) throws MyException;


}
