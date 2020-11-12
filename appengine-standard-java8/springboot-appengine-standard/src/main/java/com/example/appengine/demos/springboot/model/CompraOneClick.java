package com.example.appengine.demos.springboot.model;

import java.math.BigDecimal;

public class CompraOneClick {

	private long buyOrder;
	private String sessionId;
	private BigDecimal amount;
	private String tbkUser;
	private String userName;
	private String idCompra;
	
	public CompraOneClick(long buyOrder, String sessionId, BigDecimal amount, String tbkUser, String userName, String idCompra) {
		this.buyOrder = buyOrder;
		this.sessionId = sessionId;
		this.amount = amount;
		this.tbkUser = tbkUser;
		this.userName = userName;
		this.idCompra = idCompra;
	}
	
	public CompraOneClick() {
	}
	
	
	public String getIdCompra() {
		return idCompra;
	}

	public void setIdCompra(String idCompra) {
		this.idCompra = idCompra;
	}

	public long getBuyOrder() {
		return buyOrder;
	}
	public void setBuyOrder(long buyOrder) {
		this.buyOrder = buyOrder;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getTbkUser() {
		return tbkUser;
	}
	public void setTbkUser(String tbkUser) {
		this.tbkUser = tbkUser;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	
}
