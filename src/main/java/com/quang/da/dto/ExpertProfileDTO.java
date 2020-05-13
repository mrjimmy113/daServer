package com.quang.da.dto;

import java.sql.Date;

public class ExpertProfileDTO {

	private String email;
	
	private String password;

	private String firstname;

	private String lastname;

	private Date createdDate;

	private String address;

	private String city;

	private Date dob;

	private String primaryLanguage;

	private String major;

	private float feePerHour;

	private String description;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getPrimaryLanguage() {
		return primaryLanguage;
	}

	public void setPrimaryLanguage(String primaryLanguage) {
		this.primaryLanguage = primaryLanguage;
	}

	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	public float getFeePerHour() {
		return feePerHour;
	}

	public void setFeePerHour(float feePerHour) {
		this.feePerHour = feePerHour;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	
}
