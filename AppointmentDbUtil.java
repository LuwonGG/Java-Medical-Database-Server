package com.araya.jsf.appointments;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class AppointmentDbUtil {

	private static AppointmentDbUtil instance;
	private DataSource dataSource;
	private String jndiName = "java:comp/env/jdbc/appointments";
	
	/**
	 * Singleton Class to make sure only one instance is running at a time.
	 * @Author Luan Vu
	 * @return	The request instance.
	 * @throws 	Exception
	 */
	public static AppointmentDbUtil getInstance() throws Exception {
		
		if (instance == null) {
			instance = new AppointmentDbUtil();
		}
		
		return instance;
	}
	
	private AppointmentDbUtil() throws Exception {		
		dataSource = getDataSource();
	}

	private DataSource getDataSource() throws NamingException {
		Context context = new InitialContext();
		
		DataSource theDataSource = (DataSource) context.lookup(jndiName);
		
		return theDataSource;
	}
		
	/**
	 * Retrieves all line item appointments from sql server ordered by date.
	 * @Author: Luan Vu
	 * @return	List of all appointments.
	 */
	public List<Appointments> getAppointments() throws Exception {

		List<Appointments> appointments = new ArrayList<>();

		Connection my_conn = null;
		Statement my_stmt = null;
		ResultSet my_rs = null;
		
		try {
			my_conn = getConnection();

			String sql = "SELECT * FROM `app_log` ORDER BY app_date";

			my_stmt = my_conn.createStatement();

			my_rs = my_stmt.executeQuery(sql);

			// process result set
			while (my_rs.next()) {
				
				// retrieve data from result set row
				int log_id = my_rs.getInt("log_id");
				String first_name = my_rs.getString("first_name");
				String last_name = my_rs.getString("last_name");
				String date = my_rs.getString("app_date");
				String dx_codes = my_rs.getString("DX_codes");
				String cpt_codes = my_rs.getString("CPT_codes");
				String notes = my_rs.getString("notes");

				// create new temporary appointment object
				Appointments tempAppointment = new Appointments(log_id, first_name, last_name,
						date, dx_codes, cpt_codes, notes);

				// add it to the list of appointments
				appointments.add(tempAppointment);
			}
			
			return appointments;		
		}
		finally {
			close (my_conn, my_stmt, my_rs);
		}
	}

	/**
	 * Add/Insert entirely new appointment into SQL server database. NOTE** id is omitted to 
	 * 	avoid conflicting duplicate primary key id's in SQL server database.
	 * @Author	Luan Vu
	 * @param theAppointment	The appointment object being added to server.
	 * @throws Exception	
	 */
	public void addAppointment(Appointments theAppointment) throws Exception {

		Connection my_conn = null;
		PreparedStatement my_stmt = null;

		try {
			my_conn = getConnection();
			
			String m = theAppointment.getMonth();
			String d = theAppointment.getDay();
			String y = theAppointment.getYear();
			String sql = "INSERT INTO `app_log` (first_name, last_name, app_date, dx_codes, cpt_codes, notes) values (?, ?, ?, ?, ?, ?)";

			my_stmt = my_conn.prepareStatement(sql);

			// set params
			my_stmt.setString(1, theAppointment.getFirstName());
			my_stmt.setString(2, theAppointment.getLastName());
			my_stmt.setString(3, theAppointment.getSQLDate(m, d, y));
			my_stmt.setString(4, theAppointment.getDx_codes());
			my_stmt.setString(5, theAppointment.getCpt_codes());
			my_stmt.setString(6, theAppointment.getNotes());
			my_stmt.execute();
		}
		finally {
			close (my_conn, my_stmt);
		}
		
	}
	
	/**
	 * Edit/Update an existing appointment in SQL server database.
	 * @Author	Luan Vu
	 * @param theAppointment	The appointment to be changed.
	 * @throws Exception
	 */
	public void updateAppointment(Appointments theAppointment) throws Exception {

		Connection my_conn = null;
		PreparedStatement my_stmt = null;

		try {
			my_conn = getConnection();
			
			String m = theAppointment.getMonth();
			String d = theAppointment.getDay();
			String y = theAppointment.getYear();
			
			String sql = "UPDATE `app_log` "
						+ " SET first_name=?, last_name=?, app_date=STR_TO_DATE(?, `%m/%d/%y`), dx_codes=?, cpt_codes=?, notes=?"
						+ " WHERE log_id=?";
			my_stmt = my_conn.prepareStatement(sql);

			// set params
			my_stmt.setString(1, theAppointment.getFirstName());
			my_stmt.setString(2, theAppointment.getLastName());
			my_stmt.setString(3, theAppointment.getSQLDate(m, d, y));
			my_stmt.setString(4, theAppointment.getDx_codes());
			my_stmt.setString(5, theAppointment.getCpt_codes());
			my_stmt.setString(6, theAppointment.getNotes());
			my_stmt.setInt(7, theAppointment.getLogID());
			my_stmt.execute();
		}
		finally {
			close (my_conn, my_stmt);
		}
		
	}
	
	/**
	 * Deletes an appointment from the SQL server database based on log_id.
	 * @Author Luan Vu
	 * @param log_id	log_id of appointment.
	 * @throws Exception
	 */
	public void deleteAppointment(int log_id) throws Exception {

		Connection my_conn = null;
		PreparedStatement my_stmt = null;

		try {
			my_conn = getConnection();

			String sql = "DELETE FROM `app_log` WHERE log_id=?";

			my_stmt = my_conn.prepareStatement(sql);

			// set params
			my_stmt.setInt(1, log_id);
			
			my_stmt.execute();
		}
		finally {
			close (my_conn, my_stmt);
		}		
	}	
	
	/**
	 * Creates connection handler with SQL server database.
	 * @Author	Luan Vu
	 * @return	SQL handler connection.
	 * @throws Exception
	 */
	private Connection getConnection() throws Exception {
		
		Connection the_conn = dataSource.getConnection();
		
		return the_conn;
	}
	
	/**
	 * Closes connection handler with SQL server database.
	 * @Author	Luan Vu
	 * @param the_conn	SQL handler.
	 * @param the_stmt	SQL statement.
	 */
	private void close(Connection the_conn, Statement the_stmt) {
		
		close(the_conn, the_stmt, null);
	}
	
	/**
	 * Closes connection handler with SQL server database.
	 * @param the_conn	SQL handler.
	 * @param the_stmt	SQL statement.
	 * @param the_rs	SQL result statement.
	 */
	private void close(Connection the_conn, Statement the_stmt, ResultSet the_rs) {
		
		try {
			if (the_rs != null) {
				the_rs.close();
			}

			if (the_stmt != null) {
				the_stmt.close();
			}

			if (the_conn != null) {
				the_conn.close();
			}
			
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}	
}
