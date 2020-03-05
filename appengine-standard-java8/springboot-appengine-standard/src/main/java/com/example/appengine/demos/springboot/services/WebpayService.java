package com.example.appengine.demos.springboot.services;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;

import com.example.appengine.demos.springboot.config.GenericResponse;
import com.example.appengine.demos.springboot.model.Compra;


public interface WebpayService {
	
	ResponseEntity<GenericResponse> createRequest(Compra compra) throws Exception;
	
	void validateTransaction(HttpServletRequest httpRequest, HttpServletResponse httpResponse);
	
	ResponseEntity<GenericResponse> getQr(String token);
	
}
