package com.quang.da.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class ProblemRequestImage {
	
	@Id
	private String imageName;
	
	@ManyToOne
	@JoinColumn(name = "requestId")
	private ProblemRequest request;

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public ProblemRequest getRequestId() {
		return request;
	}

	public void setRequestId(ProblemRequest requestId) {
		this.request = requestId;
	}

	public ProblemRequestImage(String imageName, ProblemRequest requestId) {
		super();
		this.imageName = imageName;
		this.request = requestId;
	}

	public ProblemRequestImage() {
		super();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((imageName == null) ? 0 : imageName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProblemRequestImage other = (ProblemRequestImage) obj;
		if (imageName == null) {
			if (other.imageName != null)
				return false;
		} else if (!imageName.equals(other.imageName))
			return false;
		return true;
	}
	
	
	
	

	
	
	
	
}
