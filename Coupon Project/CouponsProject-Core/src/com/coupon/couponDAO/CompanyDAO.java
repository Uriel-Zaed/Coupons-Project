package com.coupon.couponDAO;

import java.util.ArrayList;

import com.coupon.beans.Company;
import com.coupon.beans.Coupon;
import com.coupon.exception.MyException;


/**
 * Company Data Access Object class. Used to create interface for Company Data Access Object.
 * @author Anna
 *
 */
public interface CompanyDAO {

	/**
	 * The method is used to create new company in the database.
	 * @author Anna
	 * @param company - Company that is going to be created in the database.
	 * @throws MyException  Exception that is thrown if there was a problem with connection or with database.
	 * This exception is also thrown if company already exists.
	 */
	public void createCompany(Company company) throws MyException;	
	public void removeCompany (Company company) throws MyException;
	public void updateCompany(Company company) throws MyException;	
	public Company getCompany (int id) throws MyException;
	public ArrayList<Company> getAllCompanies() throws MyException;
	
	public void createCompanyCoupon(Coupon coupon, long companyId) throws MyException;

	public ArrayList<Coupon> getAllCompanyCoupons(long id) throws MyException;

	public Company getCompanyByName(String name) throws MyException;
	
	public boolean login (String compName,String password) throws MyException;
	
	
	
}
