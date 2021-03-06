package com.quang.da.entity;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class ProblemRequest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer requestId;
	
	@Column
	private String title;
	
	@Column
	private String description;
	
	@ManyToOne
	@JoinColumn(name = "statusId", nullable = false)
	private Status status;
	
	@ManyToOne
	@JoinColumn(name = "customerId", nullable = false)
	private Customer customer;
	
	@ManyToOne
	@JoinColumn(name = "acceptedExpertId", nullable = true)
	private Expert expert;
	
	@Column
	private String feedBack;
	
	@Column
	private float rating;
	
	@Column
	private Date createdDate;
	
	@Column
	private Date completedDate;
	
	@Column
	private Date deadlineDate;
	
	@Column
	private float estimateHour;
	
	@Column
	private float total;
	
	@JoinColumn(name = "majorId")
	@ManyToOne
	private Major major;
	
	@OneToMany(mappedBy = "request", cascade = CascadeType.ALL)
	private List<ProblemRequestImage> images = new ArrayList<ProblemRequestImage>();
	
	


	public Major getMajor() {
		return major;
	}

	public void setMajor(Major major) {
		this.major = major;
	}

	public float getEstimateHour() {
		return estimateHour;
	}

	public void setEstimateHour(float estimateHour) {
		this.estimateHour = estimateHour;
	}

	public float getTotal() {
		return total;
	}

	public void setTotal(float total) {
		this.total = total;
	}

	public Integer getRequestId() {
		return requestId;
	}

	public void setRequestId(Integer requestId) {
		this.requestId = requestId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
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

	public String getFeedBack() {
		return feedBack;
	}

	public void setFeedBack(String feedBack) {
		this.feedBack = feedBack;
	}

	public float getRating() {
		return rating;
	}

	public void setRating(float rating) {
		this.rating = rating;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getCompletedDate() {
		return completedDate;
	}

	public void setCompletedDate(Date completedDate) {
		this.completedDate = completedDate;
	}

	public Date getDeadlineDate() {
		return deadlineDate;
	}

	public void setDeadlineDate(Date deadlineDate) {
		this.deadlineDate = deadlineDate;
	}

	public List<ProblemRequestImage> getImages() {
		return images;
	}

	public void setImages(List<ProblemRequestImage> images) {
		this.images = images;
	}
	
	
	
}
