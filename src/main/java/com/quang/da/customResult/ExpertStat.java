package com.quang.da.customResult;

import com.quang.da.enumaration.StatusEnum;

public class ExpertStat {
	private StatusEnum statusEnum;
	private long count;
	private double rating;
	
	
	
	
	public ExpertStat(StatusEnum statusEnum, long count, double rating) {
		super();
		this.statusEnum = statusEnum;
		this.count = count;
		this.rating = rating;
	}
	public StatusEnum getStatusEnum() {
		return statusEnum;
	}
	public void setStatusEnum(StatusEnum statusEnum) {
		this.statusEnum = statusEnum;
	}
	public long getCount() {
		return count;
	}
	public void setCount(long count) {
		this.count = count;
	}
	public double getRating() {
		return rating;
	}
	public void setRating(double rating) {
		this.rating = rating;
	}
	
	
	
	
}
