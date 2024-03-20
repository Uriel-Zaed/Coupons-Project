package com.coupon.couponDAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.coupon.beans.Coupon;
import com.coupon.beans.CouponType;
import com.coupon.beans.Customer;
import com.coupon.exception.MyException;
import com.mysql.jdbc.Connection;

public class CustomerDBDAO implements CustomerDAO {

	private Statement st = null;
	private ResultSet rs = null;
	private ConnectionPoolSingleton pool;

	public CustomerDBDAO() {
		this.pool = ConnectionPoolSingleton.getInstance();
	}

	@Override
	public void createCustomer(Customer customer) throws MyException {
		String query = " INSERT INTO Customer (id, customer_name, password) VALUES (?,?,?)";

		Connection con = null;
		PreparedStatement preparedStmt;

		try {
			con = (Connection) pool.getConnection();
			preparedStmt = (PreparedStatement) con.prepareStatement(query);

			preparedStmt.setLong(1, customer.getId());
			preparedStmt.setString(2, customer.getCustName());
			preparedStmt.setString(3, customer.getPassword());

			preparedStmt.execute();

			System.out.println("inserted new customer");

		} catch (SQLException e) {
			if (e.getErrorCode() == 1062)
				throw new MyException("Customer already exists in the database.");
			throw new MyException("There is a problem with database.");
		} finally {
			if (con != null) {
				pool.returnConnection(con);
			}
		}
	}

	@Override
	public void removeCustomer(Customer customer) throws MyException {

		String query = "DELETE customer_coupon.* " + "FROM company_coupon "
				+ "LEFT JOIN customer_coupon ON customer_coupon.coupon_id=company_coupon.coupon_id "
				+ "WHERE company_coupon.company_id=" + customer.getId();

		Connection con = null;

		try {
			con = (Connection) pool.getConnection();
			st = (Statement) con.createStatement();
			st.executeUpdate(query);

			query = "DELETE FROM Customer WHERE id=" + customer.getId();
			// Here we delete customer itself.
			int rowsAffected = st.executeUpdate(query);
			if (rowsAffected == 0)
				throw new MyException("Selected customer does not exist in the database.");
			System.out.println("Customer has been removed sucessfully.");
		} catch (SQLException e) {
			throw new MyException("there is a sql exception");
		} finally {
			if (con != null)
				pool.returnConnection(con);
		}

	}

	@Override
	public void updateCustomer(Customer customer) throws MyException {
		Connection con = null;
		String query = " UPDATE Customer " + " SET password = '" + customer.getPassword() + "' " + "  WHERE id= "
				+ customer.getId();

		int rowsAffected = 0;

		try {
			con = (Connection) pool.getConnection();
			st = (Statement) con.createStatement();
			rowsAffected = st.executeUpdate(query);
			if (rowsAffected == 0) {
				throw new MyException("Selected customer does not exist in the database.");
			}
		} catch (SQLException | MyException e) {

			throw new MyException("there is a sql exception");
		} finally {
			if (con != null)
				pool.returnConnection(con);
		}

		System.out.println("rows changed: " + rowsAffected);
	}

	/**
	 * A method which gets a customer id and search in the database the specific
	 * customer by his id.
	 */
	@Override
	public Customer getCustomer(int idCus) throws MyException {
		Connection con = null;
		try {
			con = (Connection) pool.getConnection();
			st = (Statement) con.createStatement();
			rs = st.executeQuery("SELECT * FROM Customer WHERE id=" + idCus);
			String custName = null;
			String password = null;

			if (rs.first()) {

				custName = rs.getString("customer_name");
				password = rs.getString("password");
				return new Customer(idCus, custName, password);

			}
			throw new MyException("Customer doesn't exist.");
		} catch (SQLException e) {
			throw new MyException("there is a problem with the database");
		} finally {
			if (con != null)
				pool.returnConnection(con);
		}

	}

	@Override
	public Customer getCustomerByName(String name) throws MyException {
		Connection con = null;
		Customer a = null;
		try {
			con = (Connection) pool.getConnection();
			st = (Statement) con.createStatement();
			String query = "SELECT * FROM Customer WHERE customer_name='" + name + "'";
			rs = st.executeQuery(query);
			long id;
			String password = null;

			if (rs.first()) {

				id = rs.getLong("id");
				password = rs.getString("password");
				a = new Customer(id, name, password);

			}
			return a;
		} catch (SQLException e) {
			throw new MyException("there is a problem with the database");
		} finally {
			if (con != null)
				pool.returnConnection(con);
		}

	}

	/**
	 * This method returns array list of all the customers.
	 * 
	 * @throws MyException
	 */
	@Override
	public ArrayList<Customer> getAllCustomer() throws MyException {
		ArrayList<Customer> allCustomers = new ArrayList<Customer>();
		Connection con = null;

		try {
			con = (Connection) pool.getConnection();
			st = (Statement) con.createStatement();
			rs = st.executeQuery("SELECT * FROM Customer");

			while (rs.next()) {
				long id = rs.getLong("id");
				String custName = rs.getString("customer_name");
				String password = rs.getString("password");

				allCustomers.add(new Customer(id, custName, password));
			}
			return allCustomers;
		} catch (SQLException e) {
			throw new MyException("there is a sql exception");
		} finally {
			if (con != null)
				pool.returnConnection(con);
		}
	}

	@Override
	public ArrayList<Coupon> getAllCoupons(long id) throws MyException {
		ArrayList<Coupon> allCoupons = new ArrayList<Coupon>();
		Connection con = null;
		String query = "SELECT Coupon.* FROM customer_coupon "
				+ "INNER JOIN Coupon ON customer_coupon.coupon_id=Coupon.id " + "WHERE customer_coupon.customer_id="
				+ id;

		try {

			con = (Connection) pool.getConnection();
			st = con.createStatement();
			rs = st.executeQuery(query);

			while (rs.next()) {
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
			throw new MyException("there is a sql exception");
		} finally {
			if (con != null)
				pool.returnConnection(con);
		}

	}

	public ArrayList<Coupon> getCouponsByType(long id, CouponType couponType) throws MyException {
		ArrayList<Coupon> allCoupons = new ArrayList<Coupon>();
		Connection con = null;
		String query = "SELECT Coupon.* FROM customer_coupon "
				+ "INNER JOIN Coupon ON customer_coupon.coupon_id=Coupon.id " + "WHERE customer_coupon.customer_id="
				+ id;

		try {

			con = (Connection) pool.getConnection();
			st = con.createStatement();
			rs = st.executeQuery(query);

			while (rs.next()) {
				Coupon coupon = null;
				String title = rs.getString("title");
				java.util.Date startDate = new java.util.Date(rs.getDate("start_date").getTime());
				java.util.Date endDate = new java.util.Date(rs.getDate("end_date").getTime());
				int amount = (int) rs.getDouble("amount");
				CouponType type = CouponType.valueOf(rs.getString("type"));
				String message = rs.getString("message");
				double price = rs.getDouble("price");
				String image = rs.getString("image");
				if (type.equals(couponType)) {
					coupon = new Coupon(id, title, startDate, endDate, amount, type, message, price, image);
					allCoupons.add(coupon);
				}
			}

			return allCoupons;

		} catch (SQLException e) {
			throw new MyException("there is a sql exception");
		} finally {
			if (con != null)
				pool.returnConnection(con);
		}

	}

	public ArrayList<Coupon> getCouponsByPrice(long id, double Selectedprice) throws MyException {
		ArrayList<Coupon> allCoupons = new ArrayList<Coupon>();
		Connection con = null;
		String query = "SELECT Coupon.* FROM customer_coupon "
				+ "INNER JOIN Coupon ON customer_coupon.coupon_id=Coupon.id " + "WHERE customer_coupon.customer_id="
				+ id;

		try {

			con = (Connection) pool.getConnection();
			st = con.createStatement();
			rs = st.executeQuery(query);

			while (rs.next()) {
				Coupon coupon = null;
				String title = rs.getString("title");
				java.util.Date startDate = new java.util.Date(rs.getDate("start_date").getTime());
				java.util.Date endDate = new java.util.Date(rs.getDate("end_date").getTime());
				int amount = (int) rs.getDouble("amount");
				CouponType type = CouponType.valueOf(rs.getString("type"));
				String message = rs.getString("message");
				double price = rs.getDouble("price");
				String image = rs.getString("image");
				if (price <= Selectedprice) {
					coupon = new Coupon(id, title, startDate, endDate, amount, type, message, price, image);
					allCoupons.add(coupon);
				}
			}

			return allCoupons;

		} catch (SQLException e) {
			throw new MyException("there is a sql exception");
		} finally {
			if (con != null)
				pool.returnConnection(con);
		}

	}

	/**
	 * this method adds purchased coupons into table customer coupon in the data
	 * base. each coupon belongs to customer , there is no coupon that belongs to
	 * different customers.
	 * 
	 * @param coupon
	 *            - inserting id of coupon.
	 * @param customer
	 *            - inserting id of customer.
	 * @throws MyException
	 */
	public void customerPurchaseCoupon(Coupon coupon, Customer customer) throws MyException {
		String query;
		Connection con = null;
		PreparedStatement preparedStmt;

		try {
			con = (Connection) pool.getConnection();
			query = "SELECT * FROM Coupon WHERE id= " + coupon.getId();
			ResultSet rs = con.createStatement().executeQuery(query);
			if(rs.first()) {
				if(rs.getInt("amount") == 0)
					throw new MyException("Not enough coupons");
			} else {
				throw new MyException("Coupon does not exist");
			}
			query = "INSERT INTO customer_coupon (customer_id, coupon_id) VALUES (?,?)";
			preparedStmt = (PreparedStatement) con.prepareStatement(query);

			preparedStmt.setLong(1, customer.getId());
			preparedStmt.setLong(2, coupon.getId());

			preparedStmt.execute();

			query = "UPDATE Coupon SET amount = amount - 1 WHERE id= " + coupon.getId();
			con.createStatement().executeUpdate(query);
			System.out.println("inserted successfully.");

		} catch (SQLException e) {
			throw new MyException("There is a problem with database or coupon already purchased.");
		} finally {
			if (con != null) {
				pool.returnConnection(con);
			}
		}

	}

	/**
	 * login method.
	 */
	@Override
	public boolean login(String custName, String password) throws MyException {

		Connection con = null;

		try {
			con = (Connection) pool.getConnection();
			String query = "SELECT * FROM Customer WHERE customer_name='" + custName + "'";
			st = (Statement) con.createStatement();

			rs = st.executeQuery(query);

			if (rs.first()) {
				String pass = rs.getString("password");
				if (pass.equals(password))
					return true;
			}
			return false;
		} catch (SQLException e) {
			throw new MyException("There is a problem with the database.");
		} finally {
			if (con != null)
				pool.returnConnection(con);
		}

	}

}
