package com.quang.da.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class ChatMessage {

	@Id
	@GeneratedValue(strategy =GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(nullable = false, name = "requestId")
	private ProblemRequest request;
	
	@ManyToOne
	@JoinColumn(nullable = false, name = "customerId")
	private Customer customer;
	
	@ManyToOne
	@JoinColumn(nullable = false, name = "expertId")
	private Expert expert;
	
	@Column
	private Timestamp timeCreated;
	
	@Column
	private String message;

	
	
	
	public ChatMessage() {
		super();
	}

	public ChatMessage(Integer id, ProblemRequest request, Customer customer, Expert expert, Timestamp timeCreated,
			String message) {
		super();
		this.id = id;
		this.request = request;
		this.customer = customer;
		this.expert = expert;
		this.timeCreated = timeCreated;
		this.message = message;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public ProblemRequest getRequest() {
		return request;
	}

	public void setRequest(ProblemRequest request) {
		this.request = request;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Expert getExpert() {
		return expert;
	}

	public void setExpert(Expert expert) {
		this.expert = expert;
	}

	public Timestamp getTimeCreated() {
		return timeCreated;
	}

	public void setTimeCreated(Timestamp timeCreated) {
		this.timeCreated = timeCreated;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
