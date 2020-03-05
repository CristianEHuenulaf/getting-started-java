package com.example.appengine.demos.springboot.model;


public class Compra {
	
	private String buyOrder;
	private String sessionId;
	private double amount;
	private String billingId;
	private String urlRedirect;

	
	
	public Compra() {}
	public Compra(String buyOrder, String sessionId, double amount ,String billingId ,String urlRedirect) {
		
		this.buyOrder = buyOrder;
		this.sessionId = sessionId;
		this.amount = amount;
		this.billingId = billingId;
		this.urlRedirect = urlRedirect;
		
	}
	public String getBuyOrder() {
		return buyOrder;
	}
	public void setBuyOrder(String buyOrder) {
		this.buyOrder = buyOrder;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	public String getBillingId() {
		return billingId;
	}
	public void setBillingId(String billingId) {
		this.billingId = billingId;
	}

	public String getUrlRedirect() {
		return urlRedirect;
	}
	public void setUrlRedirect(String urlRedirect) {
		this.urlRedirect = urlRedirect;
	}

	
}
