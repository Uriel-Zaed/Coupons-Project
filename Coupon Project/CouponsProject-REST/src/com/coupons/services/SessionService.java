package com.coupons.services;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

import com.coupon.exception.MyException;
import com.coupon.facade.AdminFacade;
import com.coupon.facade.CompanyFacade;
import com.coupon.facade.StoreFacade;
import com.coupon.facade.UserType;
import com.coupons.classesPOJO.ApplicationMessage;

@Path("SessionService")
public class SessionService {

	@Context private HttpServletRequest httpRequest;
	
	@GET
	@Path("logout")
	public void logout() {
		this.httpRequest.getSession().invalidate();
	}
	
	@GET
	@Path("CheckSession")
	public Object checkSession() {
		HttpSession session = this.httpRequest.getSession();
		if(session.getAttribute("facade") == null) 
			return UserType.GUEST;
		if(session.getAttribute("facade") instanceof AdminFacade) 
			return UserType.ADMIN;
		if(session.getAttribute("facade") instanceof CompanyFacade) 
			return UserType.COMPANY;
		return UserType.CUSTOMER;
	}
	
	@GET
	@Path("Store")
	public Object getStore() {
		try {
			return new StoreFacade().getStore();
		} catch (MyException e) {
			return new ApplicationMessage(0, "There is a problem with the store.");
		}
	}
	
	
}
