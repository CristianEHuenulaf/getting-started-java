package com.example.appengine.demos.springboot.model;

public class Billing {
	

	private String id;
	private String transactionId;
	private String token;
	private int status;
	private String qrPurchase;
	private String payFormat;
	private String idStore;

	public Billing(String id, String transactionId, String token, int status, String qrPurchase, String payFormat, String idStore) {
		this.id = id;
		this.transactionId = transactionId;
		this.token = token;
		this.status = status;
		this.qrPurchase = qrPurchase;
		this.payFormat = payFormat;
		this.idStore = idStore;
	}
	
	public String getIdStore() {
		return idStore;
	}

	public void setIdStore(String store) {
		this.idStore = store;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getQrPurchase() {
		return qrPurchase;
	}

	public void setQrPurchase(String qrPurchase) {
		this.qrPurchase = qrPurchase;
	}

	public String getPayFormat() {
		return payFormat;
	}

	public void setPayFormat(String payFormat) {
		this.payFormat = payFormat;
	}

	public Billing() {
		
	}
	
	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getToken() {
		return token;
	}
	
	public void setToken(String token) {
		this.token = token;
	}
	
}
