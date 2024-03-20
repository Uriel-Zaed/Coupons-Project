package com.coupon.couponDAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.coupon.beans.Company;
import com.coupon.beans.Coupon;
import com.coupon.beans.CouponType;
import com.coupon.exception.MyException;
import com.mysql.jdbc.Connection;

public class CompanyDBDAO implements CompanyDAO{
	
	private Statement st = null;
	private ResultSet rs = null;

	private ConnectionPoolSingleton pool;
	
	public CompanyDBDAO() {
		this.pool = ConnectionPoolSingleton.getInstance();
	}
	
	/**
	 *This method creating new company, checking if this company already exist and throws a fit exception.
	 */
	@Override
	public void createCompany(Company company) throws MyException {
		String query = " INSERT INTO Company (id, company_name, password,email) VALUES (?,?,?,?)";  
		 
		  PreparedStatement preparedStmt;
			Connection con = null;

		try {
			con = (Connection) pool.getConnection();
			preparedStmt = (PreparedStatement) con.prepareStatement(query);
		 
		
		   preparedStmt.setLong (1, company.getId()); 
		   preparedStmt.setString(2, company.getCompName()); 
		   preparedStmt.setString(3, company.getPassword());   
		   preparedStmt.setString(4, company.getEmail());   
		   
		 preparedStmt.execute(); 
		
		 System.out.println("inserted new company");
		 
	} 	catch (SQLException e) {
		if(e.getErrorCode() == 1062)
				throw new MyException("Company already exists in the database.");
		throw new MyException ("there is a sql exception");
			} finally {
				if(con!=null)
					pool.returnConnection(con);
		}
		
		
	}

	/**
	 * We delete coupons and joins between coupons first because if we delete company at the beginning
	 * and the process fails at some point, there is no way to repeat the process again.
	 * The queries are spliced to make it possible to delete info in parts.
	 */
	@Override
	public void removeCompany(Company company) throws MyException {
		String query = "DELETE customer_coupon.* "
				     + "FROM company_coupon "
				     + "LEFT JOIN customer_coupon ON customer_coupon.coupon_id=company_coupon.coupon_id "
				     + "WHERE company_coupon.company_id=" + company.getId();
		Connection con = null;
		
		try {
			con = (Connection) pool.getConnection();
			st= (Statement) con.createStatement();
			//First we delete all customer coupons that are tied to coupons of the specific company.
			st.executeUpdate(query);
			query = "DELETE coupon.*, company_coupon.* "
				  + "FROM company_coupon "
				  + "INNER JOIN coupon ON coupon.id=company_coupon.coupon_id "
				  + "WHERE company_coupon.company_id=" + company.getId();
			//Here we delete all coupon in Coupon table and company_coupon join table.
			st.executeUpdate(query);
			query= "DELETE FROM company WHERE id="+company.getId(); 
			//Here we delete company itself.
			int rowsAffected = st.executeUpdate(query);
			if(rowsAffected==0)
				throw new MyException("Selected company does not exist in the database.");
			System.out.println("Company has been removed sucessfully.");
		} catch (SQLException e) {
			throw new MyException ("there is a sql exception");			
		} finally {
			if(con!=null)
				pool.returnConnection(con);
		}		
	}
	

	@Override
	public void updateCompany(Company company) throws MyException {
		
		
		String query= " UPDATE Company "
				+ " SET password = '" + company.getPassword() + "', email='" + company.getEmail() + "'"
				+"  WHERE id= "+company.getId();
		
		Connection con = null;
		int rowsAffected = 0;
		
			try {
				con = (Connection) pool.getConnection();
				st= (Statement) con.createStatement();
				rowsAffected = st.executeUpdate(query);
				System.out.println("Company information has been updated sucessfully.");
				if(rowsAffected == 0)
					throw new MyException("Company does not exist.");
			} catch (SQLException e ){
				throw new MyException ("There is a problem with database.s");
			} finally {
				if(con!=null)
					pool.returnConnection(con);
			}	
	}

	/**
	 * method to get a specific company from the Companies table
	 */
	@Override
	public Company getCompany(int id) throws MyException{
		Connection con = null;
		Company a = null;
		
		try {
		con  = (Connection) pool.getConnection();
		st = (Statement) con.createStatement();
		rs = st.executeQuery("SELECT * FROM Company WHERE id="+id);
		
		String compName = null;
		String password = null;
		String email = null;
		
		if (rs.first())
		{
			compName = rs.getString("Company_name");	
			password = rs.getString("password");
			email = rs.getString("email");
			a = new Company (id , compName , password , email);
			return a;
		}
		else
		throw new MyException("Company does not exist in database.");
		
		} catch (SQLException e) {
			throw new MyException("There was a problem with the database.");
		} finally {
			if(con!=null) 
				pool.returnConnection(con);
		}
		
	}
	
	
	
	@Override
	public Company getCompanyByName(String cname) throws MyException{
		Connection con = null;
		Company a = null;
		
		try {
		con  = (Connection) pool.getConnection();
		st = (Statement) con.createStatement();
		String query = "SELECT * FROM Company WHERE company_name='" + cname + "'";
		rs = st.executeQuery(query);
		
		long  id ;
		String password = null;
		String email = null;
		
		if (rs.first())
		{
			id = rs.getLong("id");	
			password = rs.getString("password");
			email = rs.getString("email");
			a = new Company (id , cname , password , email);
			return a;
		}
		else
		throw new MyException("Company does not exist in database.");
		
		} catch (SQLException e) {
			e.printStackTrace();

			throw new MyException("There was a problem with the database.");
		} finally {
			if(con!=null) 
				pool.returnConnection(con);
		}
		
	}

	@Override
	//method uses to get a collection of all companies in Companies table
	public ArrayList<Company> getAllCompanies() throws MyException {
	
		String query = "SELECT * FROM Company";
		ArrayList<Company> allCompanies= new ArrayList<Company>();
		Connection con = null;	   
		try {
			con = (Connection) pool.getConnection();
			st = (Statement) con.createStatement();
			rs = st.executeQuery(query);
				
			while (rs.next()) {
				long id = rs.getLong("id");
				String compName = rs.getString("company_name");
				String password = rs.getString("password");
				String email = rs.getString("email");
				allCompanies.add(new Company(id, compName , password , email)); 
			}
			
			return allCompanies;
			
			} catch (SQLException e){
				throw new MyException ("There was a problem with the database.");
			} finally {
				if(con!=null)
					pool.returnConnection(con);
			}
	}
	

	@Override
	public ArrayList<Coupon> getAllCompanyCoupons(long id) throws MyException {
		ArrayList<Coupon> allCoupons= new ArrayList<Coupon>();
		Connection con = null;	 
		String query = "SELECT Coupon.* FROM company_coupon "
				  	 + "INNER JOIN Coupon ON company_coupon.coupon_id=Coupon.id "
				  	 + "WHERE company_coupon.company_id=" + id;
		
		try {
			
			con = (Connection) pool.getConnection();
			st = con.createStatement();
			rs = st.executeQuery (query);
			
			while (rs.next())
			{
				Coupon coupon = null;
				String title = rs.getString("title");
				java.util.Date startDate = new java.util.Date(rs.getDate("start_date").getTime());
				java.util.Date endDate = new java.util.Date(rs.getDate("end_date").getTime());
				int amount = (int) rs.getDouble("amount");
				CouponType type = CouponType.valueOf(rs.getString("type"));
				String message = rs.getString("message");
				double price = rs.getDouble("price");
				String image = rs.getString("image");
				coupon = new Coupon(rs.getLong("id"), title, startDate, endDate, amount, type, message, price, image);
				allCoupons.add(coupon);
			}
			
			return allCoupons;
			
		} catch (SQLException e) {
			throw new MyException ("there is a sql exception");
		} finally {
			if(con!=null)
				pool.returnConnection(con);
		}

	}

	/**
	 * This methods adds new coupons to the company_coupon table in the database. 
	 * It doesn't check if coupon exists in the Coupon table.
	 * The check needs to be performed on the upper level.
	 */
	@Override
	public void createCompanyCoupon(Coupon coupon,  long companyId) throws MyException{
		
		
		String query = "INSERT INTO company_coupon (company_id, coupon_id) VALUES (?,?)";
		
		Connection con = null;	
		PreparedStatement preparedStmt;
		
		try {
			con = (Connection) pool.getConnection();
			preparedStmt = (PreparedStatement) pool.getConnection().prepareStatement(query);		 
		    preparedStmt.setLong (1, companyId); 
		    preparedStmt.setLong(2, coupon.getId());  
		   
		    preparedStmt.execute(); 
		
		} catch(SQLException e) {
				if(e.getErrorCode() == 1062)
					throw new MyException("This company already owns this coupon.");
			throw new MyException("There is a problem with the database.");
		} finally {
			if(con!=null)
				pool.returnConnection(con);
		}
		
		
	}
	
	/**
	 * this method will look for author for the company name and password combination
	 * in the Companies table.
	 * if name and password match, returns the true, else returns false.
	 * @throws MyExeption if login failed.
	 */
	@Override
	public boolean login(String compName, String password) throws MyException{
		Connection con = null;
				
		try {
		con = (Connection) pool.getConnection();
		String query = "SELECT * FROM Company WHERE company_name='" + compName + "'";
		st = (Statement) con.createStatement();
		
		rs = st.executeQuery(query);
		
		if(rs.first() ) { 
			String pass = rs.getString("password");
			if( pass.equals(password))
			   return true;	
		}
		return false;
		} catch (SQLException e) {
			throw new MyException("There is a problem with the database.");
		} finally {
			if(con!=null)
				pool.returnConnection(con);
		}
		
	}

	

}
