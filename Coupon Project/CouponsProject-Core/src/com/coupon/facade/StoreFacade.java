package com.coupon.facade;

import java.util.Collection;

import com.coupon.beans.Coupon;
import com.coupon.couponDAO.CouponDAO;
import com.coupon.couponDAO.CouponDBDAO;
import com.coupon.exception.MyException;

public class StoreFacade {

	private CouponDAO couponDAO;

	public StoreFacade() {
		this.couponDAO = new CouponDBDAO();
	}

	public Collection<Coupon> getStore() throws MyException {
		return this.couponDAO.getAllCoupon();
	}

}
