package com.coupon.couponDAO;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.coupon.beans.Coupon;
import com.coupon.beans.CouponType;
import com.coupon.exception.MyException;
import com.mysql.jdbc.Connection;

public class CouponDBDAO implements CouponDAO {

	private Statement st = null;
	private ResultSet rs = null;

	private ConnectionPoolSingleton pool = null;

	public CouponDBDAO() {
		this.pool = ConnectionPoolSingleton.getInstance();
	}

	/**
	 * this method creates a new coupon
	 */
	@Override
	public void createCoupon(Coupon coupon) throws MyException {
		String query = " INSERT INTO Coupon (id, title, start_date, end_date , amount , type , message , price , image) VALUES (?,?,?,?,?,?,?,?,?)";
		Connection con = null;
		PreparedStatement preparedStmt;

		try {
			con = (Connection) pool.getConnection();
			preparedStmt = (PreparedStatement) con.prepareStatement(query);

			preparedStmt.setLong(1, coupon.getId());
			preparedStmt.setString(2, coupon.getTitle());
			preparedStmt.setDate(3, new Date(coupon.getStartDate().getTime()));
			preparedStmt.setDate(4, new Date(coupon.getEndDate().getTime()));
			preparedStmt.setInt(5, coupon.getAmount());
			preparedStmt.setString(6, coupon.getType().name());
			preparedStmt.setString(7, coupon.getMessage());
			preparedStmt.setDouble(8, coupon.getPrice());
			preparedStmt.setString(9, coupon.getImage());

			preparedStmt.execute();

			System.out.println("Coupon has been added sucessfully.");

		} catch (SQLException e) {
			if (e.getErrorCode() == 1062)
				throw new MyException("Coupon you are trying to insert already exists in the database.");
			throw new MyException("There is a problem with the database.");
		} finally {
			if (con != null)
				pool.returnConnection(con);
		}
	}

	/**
	 * this method removes coupons.???????????
	 */
	@Override
	public void removeCoupon(Coupon coupon) throws MyException {
		String query = "DELETE FROM customer_coupon WHERE coupon_id=" + coupon.getId();

		Connection con = null;

		try {
			con = (Connection) pool.getConnection();
			st = (Statement) con.createStatement();
			st.executeUpdate(query);
			query = " DELETE FROM company_coupon WHERE coupon_id=" + coupon.getId();
			st.executeUpdate(query);
			query = "DELETE FROM Coupon WHERE id=" + coupon.getId();
			int rowsAffected = st.executeUpdate(query);
			if (rowsAffected == 0)
				throw new MyException("Coupon doesn't exist in the database.");
			System.out.println("Coupon has been removed sucessfully");
		} catch (SQLException e) {
			throw new MyException("there is a sql exception");
		} finally {
			if (con != null)
				pool.returnConnection(con);
		}
	}

	/**
	 * A method which updating exciting coupon.
	 */
	@Override
	public void updateCoupon(Coupon coupon) throws MyException {

		String query = "UPDATE Coupon " + "SET end_date = '" + new java.sql.Date(coupon.getEndDate().getTime()) + "', price=" + coupon.getPrice()
				+ " " + "WHERE id= " + coupon.getId();
		Connection con = null;
		int rowsAffected = 0;

		try {
			con = (Connection) pool.getConnection();
			st = (Statement) con.createStatement();
			rowsAffected = st.executeUpdate(query);
			if (rowsAffected == 0)
				throw new MyException("Coupon does not exist in the database.");
			System.out.println("Coupon has been updated sucessfully");
		} catch (SQLException e) {
			throw new MyException("there is a sql exception");
		} finally {
			if (con != null)
				pool.returnConnection(con);
		}
	}

	/**
	 * A method that gets id coupon and returns the specific coupon by this id.
	 */
	@Override
	public Coupon getCoupon(long id) throws MyException {

		Connection con = null;
		String query = "SELECT * FROM Coupon WHERE id=" + id;
		Coupon a = null;
		String title = null;
		java.util.Date startDate = null;
		java.util.Date endDate = null;
		int amount = 0;
		CouponType type = null;
		String message = null;
		double price = 0;
		String image = null;

		try {
			con = (Connection) pool.getConnection();
			st = (Statement) con.createStatement();
			rs = st.executeQuery(query);

			if (rs.first()) {
				title = rs.getString("title");
				startDate = new java.util.Date(rs.getDate("start_date").getTime());
				endDate = new java.util.Date(rs.getDate("end_date").getTime());
				amount = (int) rs.getDouble("amount");
				type = CouponType.valueOf(rs.getString("type"));
				message = rs.getString("message");
				price = rs.getDouble("price");
				image = rs.getString("image");
				a = new Coupon(id, title, startDate, endDate, amount, type, message, price, image);
			} else
				throw new MyException("The coupon you are looking for does not exist.");
			return a;

		} catch (SQLException e) {
			e.printStackTrace();
			throw new MyException("There is problem with the database.");
		} finally {
			if (con != null)
				pool.returnConnection(con);
		}
	}

	/**
	 * This method returns array list of coupons by coupon type that was defined.
	 */
	@Override
	public ArrayList<Coupon> getCouponByType(CouponType couponType) throws MyException {
		ArrayList<Coupon> allCoupons = new ArrayList<Coupon>();
		Connection con = null;
		String title = null;
		java.util.Date startDate = null;
		java.util.Date endDate = null;
		int amount = 0;
		CouponType type = null;
		String messege = null;
		double price = 0;
		String image = null;

		try {
			con = (Connection) pool.getConnection();
			st = (Statement) con.createStatement();
			rs = st.executeQuery("SELECT * FROM Coupon WHERE type='" + couponType.name() + "'");

			while (rs.next()) {
				long id = rs.getLong("id");
				title = rs.getString("title");
				startDate = new java.util.Date(rs.getDate("start_date").getTime());
				endDate = new java.util.Date(rs.getDate("end_date").getTime());
				amount = (int) rs.getDouble("amount");
				type = CouponType.valueOf(rs.getString("type"));
				messege = rs.getString("message");
				price = rs.getDouble("price");
				image = rs.getString("image");

				allCoupons.add(new Coupon(id, title, startDate, endDate, amount, type, messege, price, image));

			}
			return allCoupons;

		} catch (SQLException | MyException e) {
			throw new MyException("there is a sql exception");
		} finally {
			if (con != null)
				pool.returnConnection(con);
		}

	}

	/**
	 * Method which returns array list of all the coupons that exist in the coupon
	 * system.
	 */
	@Override
	public ArrayList<Coupon> getAllCoupon() throws MyException {
		ArrayList<Coupon> allCoupons = new ArrayList<Coupon>();
		Connection con = null;

		String title = null;
		java.util.Date startDate = null;
		java.util.Date endDate = null;
		int amount = 0;
		CouponType type = null;
		String messege = null;
		double price = 0;
		String image = null;
		String query = "SELECT * FROM Coupon ";
		try {

			con = (Connection) pool.getConnection();
			st = (Statement) con.createStatement();
			rs = st.executeQuery(query);

			while (rs.next()) {
				long id = rs.getLong("id");
				title = rs.getString("title");
				startDate =new java.util.Date( (rs.getDate("start_date")).getTime());
				endDate = new java.util.Date((rs.getDate("end_date")).getTime());
				amount = rs.getInt("amount");
				type = CouponType.valueOf(rs.getString("type"));
				messege = rs.getString("message");
				price = rs.getDouble("price");
				image = rs.getString("image");
				allCoupons.add(new Coupon(id, title, startDate, endDate, amount, type, messege, price, image));
			}
			return allCoupons;

		} catch (SQLException | MyException e) {
			throw new MyException("there is a sql exception");
		} finally {
			if (con != null)
				pool.returnConnection(con);
		}
	}

}
