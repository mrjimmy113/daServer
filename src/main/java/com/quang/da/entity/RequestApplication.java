package com.quang.da.entity;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class RequestApplication {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	
	@JoinColumn(name = "problemRequestId")
	@ManyToOne
	private ProblemRequest problemRequest;
	
	@JoinColumn(name = "expertId")
	@ManyToOne
	private Expert expert;
	
	@Column
	private Date createdDate;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public ProblemRequest getProblemRequest() {
		return problemRequest;
	}

	public void setProblemRequest(ProblemRequest problemRequest) {
		this.problemRequest = problemRequest;
	}

	public Expert getExpert() {
		return expert;
	}

	public void setExpert(Expert expert) {
		this.expert = expert;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	
	
}
