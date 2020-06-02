package com.quang.da.chat;

public class Message {

    private boolean isExpert;
    private String message;
    private MessageType type;
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
    
    

    
}
