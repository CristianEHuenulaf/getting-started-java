package com.example.appengine.demos.springboot.model;

public class TransactionOutput {
	
	private String authCode;
	private String paymentType;
	private String amount;
	private String sharesNumber;
	private String commerceCode;
	private String buyOrder;
	
	public TransactionOutput() {}
	public TransactionOutput(String authCode, String paymentType, String amount, String sharesNumber,
			String commerceCode, String buyOrder) {
		
		
		this.authCode = authCode;
		this.paymentType = paymentType;
		this.amount = amount;
		this.sharesNumber = sharesNumber;
		this.commerceCode = commerceCode;
		this.buyOrder = buyOrder;
	}
	
	public String getAuthCode() {
		return authCode;
	}
	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}
	public String getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getSharesNumber() {
		return sharesNumber;
	}
	public void setSharesNumber(String sharesNumber) {
		this.sharesNumber = sharesNumber;
	}
	public String getCommerceCode() {
		return commerceCode;
	}
	public void setCommerceCode(String commerceCode) {
		this.commerceCode = commerceCode;
	}
	public String getBuyOrder() {
		return buyOrder;
	}
	public void setBuyOrder(String buyOrder) {
		this.buyOrder = buyOrder;
	}
	
	
	
}
