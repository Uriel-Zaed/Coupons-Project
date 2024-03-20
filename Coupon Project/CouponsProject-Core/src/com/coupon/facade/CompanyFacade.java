package com.coupon.facade;

import java.util.ArrayList;
import java.util.Date;

import com.coupon.beans.Company;
import com.coupon.beans.Coupon;
import com.coupon.beans.CouponType;
import com.coupon.couponDAO.CompanyDAO;
import com.coupon.couponDAO.CompanyDBDAO;
import com.coupon.couponDAO.CouponDAO;
import com.coupon.couponDAO.CouponDBDAO;
import com.coupon.exception.MyException;

public class CompanyFacade implements CouponClientFacade {

	private CouponDAO couponDAO = null;
	private CompanyDAO companyDAO = null;
	private Company company = null;

	public CompanyFacade() {
		this.couponDAO = new CouponDBDAO();
		this.companyDAO = new CompanyDBDAO();
	}

	@Override
	public CouponClientFacade login(String name, String password, UserType userType) throws MyException {
		if (companyDAO.login(name, password)) {
			this.company = companyDAO.getCompanyByName(name);
			return this;
		}
		return null;
	}

	/**
	 * This method is used to add to specific company new coupons. It first creates
	 * the coupon and if the creation process was successful it ties the coupon to
	 * the company.
	 * 
	 * @param coupon
	 *            Coupon that is going to be added to the specified company.
	 * @param companyId
	 *            Company to which the coupon is going to be added.
	 * @throws MyException
	 *             This exception is going to be thrown if there is a problem with
	 *             either connection to the server, database errors, coupon already
	 *             exists or company already owns specified coupon.
	 */
	public void createCoupon(Coupon coupon) throws MyException {
		couponDAO.createCoupon(coupon);
		companyDAO.createCompanyCoupon(coupon, this.company.getId());
	}

	public void removeCoupon(Coupon coupon) throws MyException {
		couponDAO.removeCoupon(coupon);
	}

	public void updateCoupon(Coupon coupon) throws MyException {
		couponDAO.updateCoupon(coupon);
	}

	public Coupon getCoupon(long id) throws MyException {
		return couponDAO.getCoupon(id);
	}

	public ArrayList<Coupon> getAllCoupons() throws MyException {
		return companyDAO.getAllCompanyCoupons(this.company.getId());
	}

	public ArrayList<Coupon> getCouponByType(CouponType couponType) throws MyException {
		ArrayList<Coupon> coupons = getAllCoupons(), couponsToSend = new ArrayList<>();
		for (Coupon coupon : coupons) {
			if(coupon.getType() == couponType)
				couponsToSend.add(coupon);
		}
		return couponsToSend;
	}
	
	public ArrayList<Coupon> getCouponsUpToDate(Date date) throws MyException {
		ArrayList<Coupon> coupons = getAllCoupons(), couponsToSend = new ArrayList<>();
		for (Coupon coupon : coupons) {
			if(coupon.getEndDate().getTime() <= date.getTime())
				couponsToSend.add(coupon);
		}
		return couponsToSend;
	}
	
	public ArrayList<Coupon> getCouponsUpToPrice(double price) throws MyException {
		ArrayList<Coupon> coupons = getAllCoupons(), couponsToSend = new ArrayList<>();
		for (Coupon coupon : coupons) {
			if(coupon.getPrice() <= price)
				couponsToSend.add(coupon);
		}
		return couponsToSend;
	}

	public Company getCurrentCompanyInformation() {
		return this.company;
	}

}
