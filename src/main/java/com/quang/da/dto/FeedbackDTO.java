package com.quang.da.dto;

public class FeedbackDTO {
	private float rating;
    private String feedback;

    public FeedbackDTO(float rating, String feedback) {
        this.rating = rating;
        this.feedback = feedback;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}
