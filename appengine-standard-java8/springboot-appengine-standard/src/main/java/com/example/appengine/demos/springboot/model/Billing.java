package com.example.appengine.demos.springboot.model;

public class Billing {
	

	private long id;
	private String idBilling;
	private String token;
	
	
	


	public Billing(long id, String idBilling, String token) {
		this.id = id;
		this.idBilling = idBilling;
		this.token = token;
	}

	public Billing() {
		
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public String getIdBilling() {
		return idBilling;
	}
	public void setIdBilling(String idBilling) {
		this.idBilling = idBilling;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	
}
