package com.quang.da.entity;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Expert extends Account implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column
	private String major;

	@Column
	private float feePerHour;

	@Column
	private String description;

	

	public Expert() {
		super();
	}

	public Expert(Integer id, String email, String password, String firstname, 
			String lastname, Date createdDate, Status status,
			String major, float feePerHour, String description) {
		super(id, email, password, firstname, lastname, createdDate,status);
		this.major = major;
		this.feePerHour = feePerHour;
		this.description = description;
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

}
