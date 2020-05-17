package com.quang.da.service.customResult;

public class CheckTokenResult {
	private boolean isExpert;
	private boolean isValid;
	
	
	
	public CheckTokenResult(boolean isExpert, boolean isValid) {
		super();
		this.isExpert = isExpert;
		this.isValid = isValid;
	}



	public boolean isExpert() {
		return isExpert;
	}



	public boolean isValid() {
		return isValid;
	}
	
	
	
	
}
