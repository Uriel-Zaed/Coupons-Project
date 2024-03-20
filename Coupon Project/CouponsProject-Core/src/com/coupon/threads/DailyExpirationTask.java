package com.coupon.threads;

import java.util.ArrayList;

import com.coupon.beans.Coupon;
import com.coupon.couponDAO.CouponDAO;
import com.coupon.couponDAO.CouponDBDAO;
import com.coupon.exception.MyException;


public class DailyExpirationTask implements Runnable{

	private CouponDAO couponDAO;
	private boolean quit;
	
	public DailyExpirationTask() {
		this.couponDAO = new CouponDBDAO();
		this.quit = false;
	}
	
	
	@Override
	public void run() {
		ArrayList<Coupon> coupons;
		try {
			coupons = couponDAO.getAllCoupon();
			
			while (!quit){
					
				for ( int i = 0; i < coupons.size(); i++) {
					if(coupons.get(i).getEndDate() != null && coupons.get(i).getEndDate().getTime() < System.currentTimeMillis()) {
							
								try {
									couponDAO.removeCoupon(coupons.get(i));
								} catch (MyException e) {
									
									e.printStackTrace();
								}
					}

				}			
				try {
					Thread.sleep(1000*60*60*24);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					return;
				}
			}
		} catch (MyException e1) {
			e1.printStackTrace();
		}
	
		
	}
	
	
	
	
	public void stopTask(){
		quit = true;
		Thread.currentThread().interrupt();
	}

}
