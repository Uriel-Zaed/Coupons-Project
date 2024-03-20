package com.coupons.services;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.coupon.beans.Coupon;
import com.coupon.beans.CouponType;
import com.coupon.couponSystem.CouponSystemSingleton;
import com.coupon.exception.MyException;
import com.coupon.facade.CompanyFacade;
import com.coupon.facade.UserType;
import com.coupons.annotations.LoginFilterAnnotation;
import com.coupons.annotations.SessionFilterAnnotation;
import com.coupons.business_delegate.BusinessDelegate;
import com.coupons.classesPOJO.ApplicationMessage;
import com.coupons.classesPOJO.LoginInfo;
import com.coupons.classesPOJO.ResponseCodes;

@Path("CompanyService")
public class CompanyService {

	@Context
	private HttpServletRequest request;

	public CompanyService() {
	}

	@Path("login")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@LoginFilterAnnotation
	public Object login(LoginInfo loginInfo) {
		try {
			CompanyFacade company = (CompanyFacade) CouponSystemSingleton.getInstance().login(loginInfo.getUserName(),
					loginInfo.getPassword(), UserType.COMPANY);
			if (company == null)
				return new ApplicationMessage(ResponseCodes.OTHER_ERROR,
						"The information you have provided is incorrect.");

			HttpSession session = request.getSession();
			session.setAttribute("facade", company);
			return new ApplicationMessage(ResponseCodes.SUCCESS, "Logged in successfully.");
		} catch (MyException e) {
			return new ApplicationMessage(ResponseCodes.SYSTEM_EXCEPTION, e.getMessage());
		}

	}

	@Path("coupon")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@SessionFilterAnnotation
	public Object createCoupon(Coupon coupon) {
		HttpSession session = request.getSession();
		CompanyFacade company = (CompanyFacade) session.getAttribute("facade");
		System.out.println(coupon);
		try {
			company.createCoupon(coupon);
			BusinessDelegate.BusinessDelegate.storeIncome(company.getCurrentCompanyInformation().getCompName(), "COMPANY_NEW_COUPON",
					100, UserType.COMPANY);
			return new ApplicationMessage(ResponseCodes.SUCCESS, "Coupon has been created successfully.");
		} catch (MyException e) {
			return new ApplicationMessage(ResponseCodes.SYSTEM_EXCEPTION, e.getMessage());
		}
	}

	@Path("coupon/{id}")
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	@SessionFilterAnnotation
	public Object removeCoupon(@PathParam("id") int id) {
		HttpSession session = request.getSession();
		CompanyFacade company = (CompanyFacade) session.getAttribute("facade");

		try {
			company.removeCoupon(company.getCoupon(id));
			return new ApplicationMessage(ResponseCodes.SUCCESS, "Coupon have been removed successfully");

		} catch (MyException e) {
			return new ApplicationMessage(ResponseCodes.SYSTEM_EXCEPTION, e.getMessage());
		}

	}

	@Path("coupon")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@SessionFilterAnnotation
	public Object updateCoupon(Coupon coupon) {
		HttpSession session = request.getSession();
		CompanyFacade company = (CompanyFacade) session.getAttribute("facade");

		try {
			company.updateCoupon(coupon);
			BusinessDelegate.BusinessDelegate.storeIncome(company.getCurrentCompanyInformation().getCompName(), "COMPANY_UPDATE_COUPON",
					10, UserType.COMPANY);
			return new ApplicationMessage(ResponseCodes.SUCCESS, "Coupon updated successfully.");
		} catch (MyException e) {
			return new ApplicationMessage(ResponseCodes.SYSTEM_EXCEPTION, e.getMessage());
		}
	}

	@Path("coupon/{id}")
	@GET
	@Consumes(MediaType.TEXT_PLAIN)
	@SessionFilterAnnotation
	public Object getCoupon(@PathParam("id") int id) {
		HttpSession session = request.getSession();
		CompanyFacade company = (CompanyFacade) session.getAttribute("facade");

		try {
			return company.getCoupon(id);
		} catch (MyException e) {
			return new ApplicationMessage(ResponseCodes.SYSTEM_EXCEPTION, e.getMessage());
		}
	}

	@Path("coupon")
	@GET
	@SessionFilterAnnotation
	public Object getAllCoupon() {
		HttpSession session = request.getSession();
		CompanyFacade company = (CompanyFacade) session.getAttribute("facade");

		try {
			return company.getAllCoupons();
		} catch (MyException e) {
			return new ApplicationMessage(ResponseCodes.SYSTEM_EXCEPTION, e.getMessage());
		}
	}

	@Path("couponByType/{couponType}")
	@GET
	@SessionFilterAnnotation
	public Object getAllCouponByType(@PathParam("couponType") CouponType couponType) {
		HttpSession session = request.getSession();
		CompanyFacade company = (CompanyFacade) session.getAttribute("facade");

		try {
			return company.getCouponByType(couponType);
		} catch (MyException e) {
			return new ApplicationMessage(ResponseCodes.SYSTEM_EXCEPTION, e.getMessage());
		}
	}
	
	@GET
	@Path("couponUpToDate")
	@SessionFilterAnnotation
	public Object getCouponUpToDate(@QueryParam("date") long date) {
		HttpSession session = request.getSession();
		CompanyFacade company = (CompanyFacade) session.getAttribute("facade");

		try {
			System.out.println(new java.util.Date(date));
			return company.getCouponsUpToDate(new java.util.Date(date));
		} catch (MyException e) {
			return new ApplicationMessage(ResponseCodes.SYSTEM_EXCEPTION, e.getMessage());
		}
	}

	@GET
	@Path("couponUpToPrice")
	@SessionFilterAnnotation
	public Object getCouponUpToPrice(@QueryParam("price") double price) {
		HttpSession session = request.getSession();
		CompanyFacade company = (CompanyFacade) session.getAttribute("facade");

		try {
			return company.getCouponsUpToPrice(price);
		} catch (MyException e) {
			return new ApplicationMessage(ResponseCodes.SYSTEM_EXCEPTION, e.getMessage());
		}
	}
	
	@GET
	@Path("company")
	@SessionFilterAnnotation
	public Object getCompanyInformation() {
		HttpSession session = request.getSession();
		CompanyFacade company = (CompanyFacade) session.getAttribute("facade");
		return company.getCurrentCompanyInformation();
	}
	
	@GET
	@Path("income")
	@SessionFilterAnnotation
	public Object getCompanyIncomeInfo() {
		HttpSession session = request.getSession();
		CompanyFacade company = (CompanyFacade) session.getAttribute("facade");
		try {
			return BusinessDelegate.BusinessDelegate.viewIncomeByCompany(company.getCurrentCompanyInformation().getCompName());
		} catch (MyException e) {
			return new ApplicationMessage(ResponseCodes.SYSTEM_EXCEPTION, e.getMessage());
		}
	}
}
