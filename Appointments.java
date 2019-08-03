package com.araya.jsf.appointments;

/**
 * Appointment
 * Description: Class implementation of appointment metadata tracking for Araya Acupuncture.
 * @author Luan Vu
 *
 */

import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class Appointments {
	
	// Class Fields
	private int log_id;
	private String firstName="";
	private String lastName="";
	private String day="";
	private String month="";
	private String year="";
	private String date="";
	private String dx_codes="";
	private String cpt_codes="";
	private List<String> form_dx_codes = new ArrayList<>();
	private List<String> form_cpt_codes = new ArrayList<>();
	private String notes="";
	
	// Drop down list fields
	private List<String> MONTH_OPTIONS = List.of("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12");
	private List<String> DAY_OPTIONS = List.of("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", 
			"16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31");
	private List<String> YEAR_OPTIONS = List.of("2019", "2020", "2021", "2022");
	private List<String> DXCODE_OPTIONS = List.of("M25.511", "M25.512", "M25.521", "M25.522", "M25.531", "M25.532", "M25.551",
			"M25.552", "M25.562", "M25.571", "M25.572", "M54.4", "M54.5", "M54.6", "M54.41", "M54.42", "M99.01", "M99.02", "M99.03",
			"M99.04", "M79.64", "M79.641", "M79.642", "M79.671", "M79.672", "G44.2");
	private List<String> CPTCODE_OPTIONS = List.of("98940", "98941", "99204", "99204", "99202", "99214", "99212", "97810", "97811",
			"97813", "97814", "97140", "97124", "97026", "97112", "97110");

	public Appointments () {
	}
	
	public Appointments (int id, String fname, String lname, String doa, String dx, String cpt, String note) {
		this.log_id = id;
		this.firstName = fname;
		this.lastName = lname;
		this.date = doa;
		this.dx_codes = dx;
		this.cpt_codes = cpt;
		this.notes = note;
	}
	
	public List<String> getForm_dx_codes() {
		return form_dx_codes;
	}

	public List<String> getForm_cpt_codes() {
		return form_cpt_codes;
	}

	// Getter methods
	public int getLogID () {
		return log_id;
	}
	
	public List<String> getMonth_options() {
		return MONTH_OPTIONS;
	}

	public List<String> getDay_options() {
		return DAY_OPTIONS;
	}

	public List<String> getYear_options() {
		return YEAR_OPTIONS;
	}

	public List<String> getDxcode_options() {
		return DXCODE_OPTIONS;
	}

	public List<String> getCptcode_options() {
		return CPTCODE_OPTIONS;
	}

	public String getFirstName () {
		return firstName;
	}

	public String getLastName () {
		return lastName;
	}

	public String getMonth () {
		return month;
	}

	public String getDay () {
		return day;
	}

	public String getYear () {
		return year;
	}

	public String getDate () {
		return date;
	}

	public String getSQLDate (String m, String d, String y) {
		return y + "//" + m + "//" + d;
	}

	public String getDx_codes () {
		return dx_codes;
	}

	public String getCpt_codes () {
		return cpt_codes;
	}

	public String getNotes () {
		return this.notes;
	}

	// Setter Methods
	public void setLogID (int id) {
		this.log_id = id;
	}
	
	public void setFirstName (String first_name) {
		this.firstName = first_name;
	}
	
	public void setLastName (String last_name) {
		this.lastName = last_name;
	}
	
	public void setMonth (String month) {
		this.month = month;
	}

	public void setDay (String day) {
		this.day = day;
	}

	public void setYear (String year) {
		this.year = year;
	}
	
	public void setDate (String date) {
		this.date = date;
	}
	
	public void setDate (String m, String d, String y) {
		String tempDate = "";
		tempDate = formatDate(m, d, y);
		this.date = tempDate;
	}
	
	public void setForm_dx_codes(List<String> form_dx_codes) {
		this.form_dx_codes = form_dx_codes;
		this.dx_codes = setDx_codes();
	}

	public void setForm_cpt_codes(List<String> form_cpt_codes) {
		this.form_cpt_codes = form_cpt_codes;
		this.cpt_codes = setCpt_codes();
	}

	public String setDx_codes () {
		String dx_result = "";
		if (this.form_dx_codes.size() > 0) {
			for (String i : form_dx_codes) 
				dx_result += i + ", ";
			dx_result = dx_result.substring(0, dx_result.length() - 2);
		}
		else
			dx_result = null;
		return dx_result;
	}
	
	public String setCpt_codes () {
		String cpt_result = "";
		if (this.form_cpt_codes.size() > 0) {
			for (String i : form_cpt_codes)
				cpt_result += i + ", ";
			cpt_result = cpt_result.substring(0, cpt_result.length() - 2);
		}
		else
			cpt_result = null;
		return cpt_result;
	}
	
	public void setNotes (String notes) {
		this.notes = notes;
	}

	// Helper Methods
	public String formatDate (String m, String d, String y) {
		return m + "//" + d + "//" + y;
	}
	
	@Override
	public String toString() {
		return "Appointment [log_id=" + log_id + ", first_name=" + firstName + ", last_name="
				+ lastName + ", date=" + date + ", dx_codes=" + dx_codes + ", cpt_codes=" 
				+ cpt_codes + ", notes=" + notes + "]";
	}
	
	
}
