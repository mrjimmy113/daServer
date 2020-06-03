package com.quang.da.chat;

public class OutputMessage {

	private boolean isExpert;
	private String message;
	private MessageType type;
	private String time;

	public OutputMessage() {
		super();
	}

	public OutputMessage(boolean isExpert, String message, MessageType type, String time) {
		super();
		this.isExpert = isExpert;
		this.message = message;
		this.type = type;
		this.time = time;
	}

	public boolean isExpert() {
		return isExpert;
	}

	public void setExpert(boolean isExpert) {
		this.isExpert = isExpert;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public MessageType getType() {
		return type;
	}

	public void setType(MessageType type) {
		this.type = type;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

}
