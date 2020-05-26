package com.quang.da.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class ExpertMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer expMesId;
	
	
	@JoinColumn(name = "expertId")
	@ManyToOne
	private Expert expert;
	
	@Column
	private String message;
	
	@Column
	private Timestamp timeCreated;



	
	public ExpertMessage(Integer expMesId, Expert expert, String message, Timestamp timeCreated) {
		super();
		this.expMesId = expMesId;
		this.expert = expert;
		this.message = message;
		this.timeCreated = timeCreated;
	}

	public Integer getExpMesId() {
		return expMesId;
	}

	public void setExpMesId(Integer expMesId) {
		this.expMesId = expMesId;
	}

	public Expert getExpert() {
		return expert;
	}

	public void setExpert(Expert expert) {
		this.expert = expert;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Timestamp getTimeCreated() {
		return timeCreated;
	}

	public void setTimeCreated(Timestamp timeCreated) {
		this.timeCreated = timeCreated;
	}



	public ExpertMessage() {
		super();
	}
	
	

}
