package com.example.appengine.demos.springboot.model;

import java.math.BigDecimal;

public class ReversaDTO {
	private String authorizationCode;
	private BigDecimal  authorizedAmount;
	private String buyOrder;
	private BigDecimal  nullifyAmount;
	
	public ReversaDTO(String authorizationCode, BigDecimal  authorizedAmount, String buyOrder, BigDecimal  nullifyAmount) {
		this.authorizationCode = authorizationCode;
		this.authorizedAmount = authorizedAmount;
		this.buyOrder = buyOrder;
		this.nullifyAmount = nullifyAmount;
	}
	
	public ReversaDTO() {
	}
	
	public String getAuthorizationCode() {
		return authorizationCode;
	}
	public void setAuthorizationCode(String authorizationCode) {
		this.authorizationCode = authorizationCode;
	}
	public BigDecimal  getAuthorizedAmount() {
		return authorizedAmount;
	}
	public void setAuthorizedAmount(BigDecimal  authorizedAmount) {
		this.authorizedAmount = authorizedAmount;
	}
	public String getBuyOrder() {
		return buyOrder;
	}
	public void setBuyOrder(String buyOrder) {
		this.buyOrder = buyOrder;
	}
	public BigDecimal  getNullifyAmount() {
		return nullifyAmount;
	}
	public void setNullifyAmount(BigDecimal  nullifyAmount) {
		this.nullifyAmount = nullifyAmount;
	}
	
}
