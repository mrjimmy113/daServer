package com.quang.da.entity;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Customer extends Account implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column
	private String address;

	@Column
	private String city;

	@Column
	private Date dob;

	@Column
	private String primaryLanguage;

	public Customer() {

	}

	public Customer(Integer id, String email, String password, String firstname, String lastname, Date createdDate,
			String address, String city, Date dob, String primaryLanguage) {
		super(id, email, password, firstname, lastname, createdDate);
		this.address = address;
		this.city = city;
		this.dob = dob;
		this.primaryLanguage = primaryLanguage;
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



}
