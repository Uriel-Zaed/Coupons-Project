package com.coupon.main;




import java.sql.Date;
import java.util.Scanner;

import com.coupon.beans.Company;
import com.coupon.beans.Coupon;
import com.coupon.beans.CouponType;
import com.coupon.beans.Customer;
import com.coupon.couponDAO.CompanyDAO;
import com.coupon.couponDAO.CompanyDBDAO;
import com.coupon.couponDAO.CustomerDAO;
import com.coupon.couponDAO.CustomerDBDAO;
import com.coupon.couponSystem.CouponSystemSingleton;
import com.coupon.exception.MyException;
import com.coupon.facade.AdminFacade;
import com.coupon.facade.CompanyFacade;
import com.coupon.facade.CustomerFacade;
import com.coupon.facade.UserType;

public class Main {
/*
	
	public static void main(String[] args) {
		
		
		CouponSystemSingleton couponSystemSingleton ;
		
		couponSystemSingleton = CouponSystemSingleton.getInstance();
		CompanyDAO companyDAO = new CompanyDBDAO();
		CustomerDAO customerDAO = new CustomerDBDAO();
		
		CompanyFacade cfa = new CompanyFacade();
		try {
			cfa.login("timi", "1234", UserType.COMPANY);
		} catch (MyException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Customer cus = new Customer (4444, "elad", "0000");
		Customer cus2 = new Customer (2222, "benny", "3333");
		Customer cus3 = new Customer (1111, "maya", "5674");
		Company comp = new Company(1987, "levis", "13014", "levis@levis.com");
		Company comp2 = new Company(1909, "zara", "4321", "zara@zara.com");
		Company comp3 = new Company(3021, "tommy", "7890", "tommy@tommy.com");
		Coupon coup = new Coupon(39390000, "vacation in santorini", new Date(2018-03-28), new Date(2018-05-28), 1, (CouponType.TRAVELING),"lets fly to heawen", 1550, null);
		Coupon coup2 = new Coupon(2828, "go eat like person!", new Date(2018-03-03), new Date(2018-03-28), 1, (CouponType.FOOD),"eat like king in our new kitchen" , 250, null);
		Coupon coup3 = new Coupon(1717, "go skin", new Date(2018-02-03), new Date(2018-03-03), 2, (CouponType.HEALTH), "new laser go do it", 1000, null);

		
		System.out.println("welcome to coupon system");
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		System.out.println("for admin press 1 , for company press 2 , for customer press 3");
		int num = sc.nextInt();
		System.out.println("please enter name");
		String un = sc.next();
		System.out.println("please enter password");
		String pw = sc.next();
		
		
		switch(num) {
		case 1: 
			try {
				AdminFacade af = (AdminFacade) couponSystemSingleton.login(un, pw, UserType.ADMIN);
				//af.createCompany(comp);
				System.out.println("welcome to admin menu bar!!");
				System.out.println("for create new company press 1");
				System.out.println("for remove company press 2 ");
				System.out.println("for get specific company press 3");
				System.out.println("for get all companies press 4");
				System.out.println("for creat new customer press 5");
				System.out.println("for remove customer press 6");
				System.out.println("for get specific customer press 7");
				System.out.println("for get all customers press 8");
				num = sc.nextInt();
				
				switch (num) {
				case 1:
					af.createCompany(comp);
					af.createCompany(comp2);
					af.createCompany(comp3);
					break;
				case 2:
					af.removeCompany(comp);
					break;
				case 3:
					try {
						System.out.println(af.getCompany(1));
						break;
					} catch (MyException e) {
						System.out.println("problem with data base");
					}
					break;
				case 4:
					System.out.println(af.getAllCompanys());
					break;
				case 5:
					af.createCustomer(cus);
					af.createCustomer(cus2);
					af.createCustomer(cus3);
					break;
				case 6:
					af.removeCustomer(cus);
					break;
				case 7:
					try {
						System.out.println(af.getCustomer(2222));
						break;
					} catch (MyException e) {
						System.out.println("problem with data base");
					}
					break;
				case 8:
					System.out.println(af.getAllCustomers());
					break;
				}
				break;
			} catch (MyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

		case 2:  
			try {
				CompanyFacade cf = (CompanyFacade) couponSystemSingleton.login(un, pw, UserType.COMPANY);
				companyDAO = new CompanyDBDAO();
				Company a = 	companyDAO.getCompanyByName(un);
				System.out.println("welcome to company menu bar!!");
				System.out.println("for create new coupon press 1");
				System.out.println("for remove coupon press 2 ");
				System.out.println("for get specific coupon press 3");
				System.out.println("for get all coupons press 4");
				System.out.println("for get coupon by type press 5");
				num = sc.nextInt();
				switch (num) {
				case 1:
					cf.createCoupon(coup);
					cf.createCoupon(coup2);
					cf.createCoupon(coup3);

					break;
				case 2:
					cf.removeCoupon(coup3);
					break;
				case 3:	
					try {
						System.out.println(cf.getCoupon(coup.getId()));
					} catch (MyException e) {
						System.out.println("problem with data base");
					}
					break;
				case 4:	
					System.out.println(cf.getAllCoupons());
					break;
				case 5:
					System.out.println(cf.getCouponByType(CouponType.FOOD));
					break;
					
				}	
			} catch (MyException e) {
				e.printStackTrace();
			//	System.out.println("problem with data base");

			}
		  	break;		   
		default:   
			try {
				couponSystemSingleton.login(un, pw, UserType.CUSTOMER);
				CustomerFacade cusf = new CustomerFacade();
				Customer cust = customerDAO.getCustomerByName(un);
				System.out.println("welcome to customer menu bar!!");
				System.out.println("for purchase coupon press 1");
				System.out.println("for get all purchased coupons press 2 ");
				System.out.println("for get all purchased coupons by type press 3");
				System.out.println("for get all purchased coupons by price press 4");
				num = sc.nextInt();
				switch (num) {
				case 1:
					cusf.purchaseCoupon(coup2, cust);
					break;
				case 2:
					System.out.println(cusf.getAllPurchasedCoupons(cust));
					break;
				case 3:
					System.out.println(cusf.getAllPurchasedCouponsByType(cust, CouponType.FOOD));
					break;
				case 4:
					System.out.println(cusf.getAllPurchasedCouponsByPrice(cust, 1000));
					break;
					
				}
			} catch (MyException e) {
				System.out.println("problem with data base");

			}
			break;
			
			
			
		}

	

	}
	*/
}
