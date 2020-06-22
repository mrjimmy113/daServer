package com.quang.da.dto;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class ExpertProfileDTO {

	private Integer id;
	
	
	private String email;
	
	private String password;

	private String fullName;



	private Date createdDate;


	private List<MajorDTO> major = new ArrayList<MajorDTO>();


	private float feePerHour;

	private String description;
	
	private String bankName;
	
	private String bankAccountNo;
	
	
	private String imgName;
	
	

	public String getImgName() {
		return imgName;
	}

	public void setImgName(String imgName) {
		this.imgName = imgName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}



	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public List<MajorDTO> getMajor() {
		return major;
	}

	public void setMajor(List<MajorDTO> major) {
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

	

	
}
