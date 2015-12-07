package com.fntech.m10.u1.model;

public class Message {

	private int code; // 0 Successful, 1 Failure
	private String message;
	private String result;
	
	public Message(){
	}
	
	public int getCode() {
		return code;
	}
	public String getMessage() {
		return message;
	}
	public void setCode(int code) {
		this.code = code;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getResult() {
		return result;
	}
	
	
}
