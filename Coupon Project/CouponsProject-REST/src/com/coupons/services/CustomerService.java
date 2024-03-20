package com.coupons.services;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.coupon.beans.Coupon;
import com.coupon.beans.CouponType;
import com.coupon.couponSystem.CouponSystemSingleton;
import com.coupon.exception.MyException;
import com.coupon.facade.CustomerFacade;
import com.coupon.facade.UserType;
import com.coupons.annotations.LoginFilterAnnotation;
import com.coupons.annotations.SessionFilterAnnotation;
import com.coupons.business_delegate.BusinessDelegate;
import com.coupons.classesPOJO.ApplicationMessage;
import com.coupons.classesPOJO.LoginInfo;
import com.coupons.classesPOJO.ResponseCodes;

@Path("CustomerService")
public class CustomerService {
	
	@Context
	private HttpServletRequest request;
	
	public CustomerService() {	}
	
	
	@Path("login")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@LoginFilterAnnotation
	public Object login (LoginInfo loginInfo) {
		try {
		CustomerFacade customer =(CustomerFacade) CouponSystemSingleton.getInstance().login(loginInfo.getUserName(),
				loginInfo.getPassword(), UserType.CUSTOMER);
		if (customer == null)
			return new ApplicationMessage(ResponseCodes.OTHER_ERROR, "The information you have provided is incorrect.");
		
		HttpSession session = request.getSession();
		session.setAttribute("facade" , customer);
		return new ApplicationMessage(ResponseCodes.SUCCESS, "Logged in successfully.");
		} catch (MyException e) {
			return new ApplicationMessage(ResponseCodes.SYSTEM_EXCEPTION, e.getMessage());
		}
	}	
	
	@POST
	@Path("coupon")
	@Produces(MediaType.APPLICATION_JSON)
	@SessionFilterAnnotation
	public Object purchaseCoupon (Coupon coupon) {
		HttpSession session = request.getSession();
		CustomerFacade customer = (CustomerFacade) session.getAttribute("facade");
		System.out.println(coupon);
		try {
			customer.purchaseCoupon(coupon);
			BusinessDelegate.BusinessDelegate.storeIncome(customer.getCustomerInfo().getCustName(), "CUSTOMER_PURCHASE",
					coupon.getPrice(), UserType.CUSTOMER);
			return new ApplicationMessage(ResponseCodes.SUCCESS, "Coupon has been purchased successfully.");
		} catch (MyException e) {
			return new ApplicationMessage(ResponseCodes.SYSTEM_EXCEPTION, e.getMessage());
		}
	}
	
	
	@GET
	@Path("coupon")
	@Produces(MediaType.APPLICATION_JSON)
	@SessionFilterAnnotation
	public Object getAllPurchasedCoupons () {
		HttpSession session = request.getSession();
		CustomerFacade customer = (CustomerFacade) session.getAttribute("facade");
		try {
			return customer.getAllPurchasedCoupons();
		} catch (MyException e) {
			return new ApplicationMessage(ResponseCodes.SYSTEM_EXCEPTION, e.getMessage());
		}
	}
	
	@GET
	@Path("couponByType")
	@Produces(MediaType.APPLICATION_JSON)
	@SessionFilterAnnotation
    public Object getAllPurchasedCouponsByType (@QueryParam("type") CouponType couponType) {
    	HttpSession session = request.getSession();
		CustomerFacade customer = (CustomerFacade) session.getAttribute("facade");
		try {
			return customer.getAllPurchasedCouponsByType(couponType);
		} catch (MyException e) {
			return new ApplicationMessage(ResponseCodes.SYSTEM_EXCEPTION, e.getMessage());
		}
	}

	@GET
	@Path("couponByPrice")
	@Produces(MediaType.APPLICATION_JSON)
	@SessionFilterAnnotation
    public Object getAllPurchasedCouponsByPrice (@QueryParam("price") double price) {
    	HttpSession session = request.getSession();
		CustomerFacade customer = (CustomerFacade) session.getAttribute("facade");
		try {
			return customer.getAllPurchasedCouponsByPrice(price);
		} catch (MyException e) {
			return new ApplicationMessage(ResponseCodes.SYSTEM_EXCEPTION, e.getMessage());
		}
    }
	
	
    @GET
    @Path("customerInfo")
    @Produces(MediaType.APPLICATION_JSON)
    @SessionFilterAnnotation
    public Object getCustomerInfo() {
    	HttpSession session = request.getSession();
		CustomerFacade customer = (CustomerFacade) session.getAttribute("facade");
		return customer.getCustomerInfo();
    }
    
    
	
}
