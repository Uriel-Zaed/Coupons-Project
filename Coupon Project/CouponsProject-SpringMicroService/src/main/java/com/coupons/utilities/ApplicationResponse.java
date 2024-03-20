package com.coupons.utilities;

public class ApplicationResponse {

	private int id;
	private String message;

	public ApplicationResponse(int id, String message) {
		this.id = id;
		this.message = message;
	}

	public ApplicationResponse() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
