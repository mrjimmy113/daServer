package com.quang.da.entity;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Expert extends Account implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JoinColumn(name = "majorId")
	@ManyToOne
	private Major major;

	@Column
	private float feePerHour;

	@Column
	private String description;
	
	@Column
	private String bankName;
	
	@Column
	private String bankAccountNo;

	

	public Expert() {
		super();
	}

	public Expert(Integer id, String email, String password, String firstname, 
			String lastname, Date createdDate, Status status,
			Major major, float feePerHour, String description, String bankName, String bankAccountNo) {
		super(id, email, password, firstname, lastname, createdDate,status);
		this.major = major;
		this.feePerHour = feePerHour;
		this.description = description;
		this.bankName = bankName;
		this.bankAccountNo = bankAccountNo;
	}

	

	public Major getMajor() {
		return major;
	}

	public void setMajor(Major major) {
		this.major = major;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankAccountNo() {
		return bankAccountNo;
	}

	public void setBankAccountNo(String bankAccountNo) {
		this.bankAccountNo = bankAccountNo;
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
