package com.example.appengine.demos.springboot.model;

import javax.xml.datatype.XMLGregorianCalendar;

import com.transbank.webpay.wswebpay.service.WsTransactionDetailOutput;

public class TransactionResult {
	
	private String buyOrder;
	private String sessionId;
	private String cardNumber;
	private String cardExpirationDate;
	private String accoutingDate;
	private XMLGregorianCalendar transactionDate;
	private String vci;
	private String urlRedirect;
	private WsTransactionDetailOutput transactionOutput;
	
	public TransactionResult() {}
	public TransactionResult(String buyOrder, String sessionId, String cardNumber, String cardExpirationDate,
			String accoutingDate, XMLGregorianCalendar transactionDate, String vci, String urlRedirect,
			WsTransactionDetailOutput transactionOutput) {
		
		
		this.buyOrder = buyOrder;
		this.sessionId = sessionId;
		this.cardNumber = cardNumber;
		this.cardExpirationDate = cardExpirationDate;
		this.accoutingDate = accoutingDate;
		this.transactionDate = transactionDate;
		this.vci = vci;
		this.urlRedirect = urlRedirect;
		this.transactionOutput = transactionOutput;
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
	public String getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	public String getCardExpirationDate() {
		return cardExpirationDate;
	}
	public void setCardExpirationDate(String cardExpirationDate) {
		this.cardExpirationDate = cardExpirationDate;
	}
	public String getAccoutingDate() {
		return accoutingDate;
	}
	public void setAccoutingDate(String accoutingDate) {
		this.accoutingDate = accoutingDate;
	}
	public XMLGregorianCalendar getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(XMLGregorianCalendar transactionDate) {
		this.transactionDate = transactionDate;
	}
	public String getVci() {
		return vci;
	}
	public void setVci(String vci) {
		this.vci = vci;
	}
	public String getUrlRedirect() {
		return urlRedirect;
	}
	public void setUrlRedirect(String urlRedirect) {
		this.urlRedirect = urlRedirect;
	}
	public WsTransactionDetailOutput getTransactionOutput() {
		return transactionOutput;
	}
	public void setTransactionOutput(WsTransactionDetailOutput transactionOutput) {
		this.transactionOutput = transactionOutput;
	}
	
	
	
	
	

	   
	
	
}
