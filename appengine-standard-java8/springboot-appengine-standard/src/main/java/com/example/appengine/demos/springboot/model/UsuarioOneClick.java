package com.example.appengine.demos.springboot.model;

public class UsuarioOneClick {
	
	private String userName;
	private String email;
	private String urlRedirect;
	
	public UsuarioOneClick(String userName, String email, String urlRedirect) {
		this.userName = userName;
		this.email = email;
		this.urlRedirect = urlRedirect;
	}
	
	public UsuarioOneClick() {}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getUrlRedirect() {
		return urlRedirect;
	}
	public void setUrlRedirect(String urlRedirect) {
		this.urlRedirect = urlRedirect;
	}
	
	

}
