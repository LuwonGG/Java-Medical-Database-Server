package com.araya.jsf.appointments;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class AppointmentController {

	private List<Appointments> appointments;
	private AppointmentDbUtil appointmentDbUtil;
	private Logger logger = Logger.getLogger(getClass().getName());
	
	public AppointmentController() throws Exception {
		appointments = new ArrayList<>();
		
		appointmentDbUtil = AppointmentDbUtil.getInstance();
	}
	
	public List<Appointments> getAppointments() {
		return appointments;
	}

	public void loadAppointments() {

		logger.info("Loading appointments");
		
		appointments.clear();

		try {
			
			// get all students from database
			appointments = appointmentDbUtil.getAppointments();
			
		} catch (Exception exc) {
			// send this to server logs
			logger.log(Level.SEVERE, "Error loading appointments", exc);
			
			// add error message for JSF page
			addErrorMessage(exc);
		}
	}
	
	public String addAppointment(Appointments theAppointment) {

		logger.info("Adding appointment: " + theAppointment);

		try {
			
			// add student to the database
			appointmentDbUtil.addAppointment(theAppointment);
			
		} catch (Exception exc) {
			// send this to server logs
			logger.log(Level.SEVERE, "Error adding appointments", exc);
			
			// add error message for JSF page
			addErrorMessage(exc);

			return null;
		}
		
		return "list-appointments?faces-redirect=true";
	}
				
	private void addErrorMessage(Exception exc) {
		FacesMessage message = new FacesMessage("Error: " + exc.getMessage());
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
	
}
