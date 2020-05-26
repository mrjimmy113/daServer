package com.quang.da.entity;

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
	@JoinColumn(nullable = false, name = "cusMesId")
	private CustomerMessage customer;
	
	@ManyToOne
	@JoinColumn(nullable = false, name = "expMesId")
	private ExpertMessage expert;
	
	
	
	
	public ChatMessage() {
		super();
	}

	
	


	public ChatMessage(Integer id, ProblemRequest request, CustomerMessage customer, ExpertMessage expert) {
		super();
		this.id = id;
		this.request = request;
		this.customer = customer;
		this.expert = expert;
	}





	public CustomerMessage getCustomer() {
		return customer;
	}





	public void setCustomer(CustomerMessage customer) {
		this.customer = customer;
	}





	public ExpertMessage getExpert() {
		return expert;
	}





	public void setExpert(ExpertMessage expert) {
		this.expert = expert;
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


	
	
}
