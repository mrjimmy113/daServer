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
public class CustomerMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer cusMesId;
	
	
	@JoinColumn(name = "customerId")
	@ManyToOne
	private Customer customer;
	
	@Column
	private String message;
	
	@Column
	private Timestamp timeCreated;

	public Integer getCusMesId() {
		return cusMesId;
	}

	public void setCusMesId(Integer cusMesId) {
		this.cusMesId = cusMesId;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
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

	public CustomerMessage(Integer cusMesId, Customer customer, String message, Timestamp timeCreated) {
		super();
		this.cusMesId = cusMesId;
		this.customer = customer;
		this.message = message;
		this.timeCreated = timeCreated;
	}

	public CustomerMessage() {
		super();
	}
	
	

}
