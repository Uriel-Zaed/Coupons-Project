package com.coupon.couponSystem;

import com.coupon.couponDAO.ConnectionPoolSingleton;
import com.coupon.exception.MyException;
import com.coupon.facade.AdminFacade;
import com.coupon.facade.CompanyFacade;
import com.coupon.facade.CouponClientFacade;
import com.coupon.facade.CustomerFacade;
import com.coupon.facade.UserType;
import com.coupon.threads.DailyExpirationTask;

public class CouponSystemSingleton {

	
	
	private DailyExpirationTask task = new DailyExpirationTask();
	private Thread expirationTaskWrapThread;
	private static CouponSystemSingleton couponSystemSingleton;
	
	
	private CouponSystemSingleton() {
		ConnectionPoolSingleton.getInstance();
		this.expirationTaskWrapThread = new Thread(task);
		this.expirationTaskWrapThread.start();
	}
	
	
	public static CouponSystemSingleton getInstance() {
		if(couponSystemSingleton == null)
			couponSystemSingleton = new CouponSystemSingleton();
		return couponSystemSingleton;
	}
	
	
	public CouponClientFacade login(String name, String password, UserType userType) throws MyException  {
		CouponClientFacade newClient = null;
		
		switch(userType) {
		case ADMIN:    newClient = new AdminFacade();					   	
					   return newClient.login(name, password, userType);
		case COMPANY:  newClient = new CompanyFacade();		
		  			   return newClient.login(name, password, userType);
		default:       newClient = new CustomerFacade();
					   return newClient.login(name, password, userType);
		}
		
	}
	
	
	public void shutDown() throws MyException {
		task.stopTask();
		ConnectionPoolSingleton.closeAllConnections();
		System.out.println("all connections closed.");
		
	}
}
