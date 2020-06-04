package com.quang.da.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.quang.da.chat.MessageType;

@Entity
public class ChatMessage {

	@Id
	@GeneratedValue(strategy =GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(nullable = false, name = "requestId")
	private ProblemRequest request;
	
	@ManyToOne
	@JoinColumn(name = "cusId")
	private Customer customer;
	
	@ManyToOne
	@JoinColumn(name = "expId")
	private Expert expert;
	
	private String message;
	
	private Timestamp time;
	
	@Enumerated(EnumType.STRING)
	private MessageType messageType;
	
	
	
	
	
	
	
	public MessageType getMessageType() {
		return messageType;
	}










	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}










	public ChatMessage() {
		super();
	}

	
	


	




	public String getMessage() {
		return message;
	}










	public void setMessage(String message) {
		this.message = message;
	}










	public Timestamp getTime() {
		return time;
	}










	public void setTime(Timestamp time) {
		this.time = time;
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
