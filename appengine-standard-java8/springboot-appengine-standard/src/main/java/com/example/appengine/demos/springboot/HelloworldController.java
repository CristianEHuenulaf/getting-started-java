/*
 * Copyright 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.appengine.demos.springboot;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.appengine.demos.springboot.services.OneClickServiceImpl;
import com.example.appengine.demos.springboot.services.WebpayMallServiceImpl;
import com.example.appengine.demos.springboot.config.GenericResponse;
import com.example.appengine.demos.springboot.model.Compra;
import com.example.appengine.demos.springboot.model.CompraOneClick;
import com.example.appengine.demos.springboot.model.ReversaDTO;
import com.example.appengine.demos.springboot.model.UsuarioOneClick;
import com.example.appengine.demos.springboot.services.WebpayServiceImpl;

@RestController
@Controller
public class HelloworldController {

	@Autowired
	private WebpayServiceImpl webpayService;

	@Autowired
	private WebpayMallServiceImpl webpayMallService;

	@Autowired
	private OneClickServiceImpl oneClickService;

	@GetMapping("/hello")
	public String hello() {
		return "Hello world - Proyecto registro de ventas!";
	}

	@CrossOrigin
	@PostMapping("/get-token")
	public ResponseEntity<GenericResponse> webPayRequest(@RequestBody Compra compra) throws Exception {
		return webpayService.createRequest(compra);
	}
	
	@CrossOrigin
	@PostMapping("/get-tokenNew")
	public ResponseEntity<GenericResponse> webPayRequestNew(@RequestBody Compra compra) throws Exception {
		return webpayService.createRequestNew(compra);
	}

	@CrossOrigin
	@PostMapping("/webpay-result")
	public void webPayEnd(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {

		webpayService.validateTransaction(httpRequest, httpResponse);

	}
	
	@CrossOrigin
	@PostMapping("/webpay-resultNew")
	public void webPayEndNew(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {

		webpayService.validateTransactionNew(httpRequest, httpResponse);

	}

	@CrossOrigin
	@PostMapping("/get-qr")
	public ResponseEntity<GenericResponse> getQr(@RequestBody String token) {

		return webpayService.getQr(token);
	}

	@CrossOrigin
	@PostMapping("webpay-mall/get-token")
	public ResponseEntity<GenericResponse> webPayMallRequest(@RequestBody Compra compra) throws Exception {
		return webpayMallService.createRequest(compra);
	}

	@CrossOrigin
	@PostMapping("webpay-mall/webpay-result")
	public void webPayMallEnd(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {

		webpayMallService.validateTransaction(httpRequest, httpResponse);

	}

	@CrossOrigin
	@PostMapping("oneClick/registerUser")
	public ResponseEntity<GenericResponse> registerUser(@RequestBody UsuarioOneClick user) throws Exception {
		return oneClickService.createRequest(user);
	}

	@CrossOrigin
	@PostMapping("oneClick/confirmRegister")
	public void registerUser(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws Exception {
		oneClickService.confirmRegisterUser(httpRequest, httpResponse);
	}

	@CrossOrigin
	@PostMapping("oneClick/registrarPago")
	public ResponseEntity<GenericResponse> registrarPago(@RequestBody CompraOneClick compra) throws Exception {
		return oneClickService.realizaPago(compra);
	}
	
	@CrossOrigin
	@PostMapping("oneClick/registrarPagoNew")
	public ResponseEntity<GenericResponse> registrarPagoNew(@RequestBody CompraOneClick compra) throws Exception {
		return oneClickService.realizaPagoNew(compra);
	}

	@CrossOrigin
	@PostMapping("oneClick/removerUsuario")
	public ResponseEntity<GenericResponse> removerUsuario(@RequestBody CompraOneClick compra) throws Exception {
		return oneClickService.removerUsuario(compra);
	}

	@CrossOrigin
	@PostMapping("oneClick/reversarPago")
	public ResponseEntity<GenericResponse> reversarPago(@RequestBody CompraOneClick compra) throws Exception {
		return oneClickService.reversarPago(compra);
	}
	
	@CrossOrigin
	@PostMapping("anularPago")
	public ResponseEntity<GenericResponse> anularPago(@RequestBody ReversaDTO compra) throws Exception {
		return oneClickService.anularPago(compra);
	}

}
