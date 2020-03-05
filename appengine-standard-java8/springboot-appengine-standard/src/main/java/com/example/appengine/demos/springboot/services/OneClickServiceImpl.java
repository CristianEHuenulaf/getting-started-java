package com.example.appengine.demos.springboot.services;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.appengine.demos.springboot.config.GenericResponse;
import com.example.appengine.demos.springboot.model.CompraOneClick;
import com.example.appengine.demos.springboot.model.UsuarioOneClick;
import com.example.appengine.demos.springboot.util.Util;
import com.transbank.webpayserver.webservices.OneClickFinishInscriptionOutput;
import com.transbank.webpayserver.webservices.OneClickInscriptionOutput;
import com.transbank.webpayserver.webservices.OneClickPayOutput;
import com.transbank.webpayserver.webservices.OneClickReverseInput;
import com.transbank.webpayserver.webservices.OneClickReverseOutput;

import cl.transbank.webpay.Webpay;
import cl.transbank.webpay.WebpayOneClick;
import cl.transbank.webpay.configuration.Configuration;
import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;

@Service
public class OneClickServiceImpl {
	
	private Configuration generarConfiguracionTransBank() {
		Configuration configuration = new Configuration();
		configuration.setEnvironment(Webpay.Environment.PRODUCCION);
		// a continuación va tu código de comercio, Si el código que posees es de 8 dígitos debes anteponer 5970.
		configuration.setCommerceCode("597035165258");
		configuration.setPrivateKey( // pega acá la llave privada de tu certificado
		    "-----BEGIN RSA PRIVATE KEY-----\r\n" + 
		    "MIIEpAIBAAKCAQEAnDKjZ09128AiK9azhj22YhnKgUXbxej3uVZMjL6Z6RVfCNnO\r\n" + 
		    "xeEgRNi4vSPRi4hXC1nBvQju0ezKQjqL52AQ/K9YQMhaDxpWUfJ4teMIQ73KHT9X\r\n" + 
		    "y9a4VGoZ6QJBpvMNwuwXV5kJF6vSftyh2cDl6GPPwBkkMcgofl4TsK6HgRZhutwK\r\n" + 
		    "BDUNDfGg5JAXCZGZht48OIeDx805aGs7qxMM9mf94WKAW/lUrQXs2Js1N08I4/8B\r\n" + 
		    "H0SXFD/Z0EWEHZ3UECZq0DORujcwvzyH3PT5aJGyG7NG1IhDMDfFMK3S4YNWvb9s\r\n" + 
		    "ECB/K4I0Cac3tAVaS9mckzNMzwIbnYcZo0R1bQIDAQABAoIBAQCPBmdNsBrxj6s6\r\n" + 
		    "jSxx3Yxlo8wkNz2YqUw8r2ME9XqlTWZqClcmJpCPugrfVi8IH13lDNk0KTvViYdi\r\n" + 
		    "wY1d3j9vwXeESnD8p1cnTmlt02e778tkoJ3aDtFWqpjzHZfUMXcGZ3ywP3dt722d\r\n" + 
		    "sJWbeOCMvOh8tAjKdMdvfuF0xTta3R9HnZEMkslT5ZL9WyXGZ1aSO3mSzdLajPZn\r\n" + 
		    "JNYCXQOCr2HIIHQx4rxdDRO70sGwbHynIBqNXPwHRJ6YGBYHui71r7o2OyTXkNRj\r\n" + 
		    "34POCZUxwNZzMgZbxWCEr2C/VP3cx81MLRVMR4YGSkDN2lIeMjLB8gr0voexRUkw\r\n" + 
		    "fFyu5zWBAoGBAM9oLKp3kdN+dq6Zqp2lSJj5ZdQ2a0aNCBJ/1GAF3rbd5IXSgmT0\r\n" + 
		    "Zxg4IYu7foD+MdNbjPyTAVTurwSoExXpwGTyyvh8jvTy0qHpKaPjZdjP0Sb7FGrO\r\n" + 
		    "q1Gj0WGIYglMCQzYaukc1hGzCsXo7dPz7bKADXuuItCr9qT6bM8CsU8dAoGBAMDL\r\n" + 
		    "DLWiwMVyX8KPoy0pOPdvD/n88Up+vqEpb2A8EeKO0l6++0nRFNdyvpRi/IPigYOi\r\n" + 
		    "wStwSd+K2GYeo2sFX6mGUof7HDDsiJr/7odloHwJODputgrl6YSysz0XE1Ohlf3O\r\n" + 
		    "7qa6NXAVBqI29kph/htaB4p3HagEcfa+p3bM0l6RAoGAFs4Xiy7Wg/C10ADpd6gC\r\n" + 
		    "0X8qqSjKptk+9/nCOkSq9mRzihtZZwxQgaM6XQjiogBRLjI00FjBnrsX12U7o/8t\r\n" + 
		    "vmvL0YKwnVJNwqvGflS0kCBZmPWmiw/qWnGuxhCe5vLLU/Q/3R3kk9Gh75zq+ZEq\r\n" + 
		    "o6Pc16qP/W3LEtzONfzPzikCgYAymFcCh+7NImHtHmL1ZB8WGGh5856jLTX793LK\r\n" + 
		    "L5mF+nNM+BPcYGl2zR8ktAGO3u+cSyckrjhE2RxHWH0MhgF/7MdV9lmEoV6MRuQj\r\n" + 
		    "VlFSm9zMwOc3XvT0pS0SuC6TQRGq5RaOXMnPW0U9Zu0lwie8p9jYxr/aqyf5WDl8\r\n" + 
		    "nO4McQKBgQC9HnD8ZKqbOSER1z1hkjqpbTiRDCO7dXSRyHEH5RWcRmUGY4EkyJSP\r\n" + 
		    "rN/yMp9r6fh5qa8BQrLQBq7Bd/8SRuquLxqx5lVTod3jXq+wTQprd/gHO9LnXkDv\r\n" + 
		    "rlM3IVhi7V9w6rk6PQ8EUNwRUcE7uLQbvKczSJjbAfaPLDo00nbeew==\r\n" + 
		    "-----END RSA PRIVATE KEY-----\r\n" + 
		    "");
		configuration.setPublicCert( // pega acá tu certificado público
		    "-----BEGIN CERTIFICATE-----\r\n" + 
		    "MIIDPzCCAicCFHXTryAB35bfajPgOOFi6lVQ5pepMA0GCSqGSIb3DQEBCwUAMFwx\r\n" + 
		    "CzAJBgNVBAYTAkFVMRMwEQYDVQQIDApTb21lLVN0YXRlMSEwHwYDVQQKDBhJbnRl\n" + 
		    "cm5ldCBXaWRnaXRzIFB0eSBMdGQxFTATBgNVBAMMDDU5NzAzNTE2NTI1ODAeFw0x\n" + 
		    "OTEyMTMxMzU3MTFaFw0yMzEyMTIxMzU3MTFaMFwxCzAJBgNVBAYTAkFVMRMwEQYD\n" + 
		    "VQQIDApTb21lLVN0YXRlMSEwHwYDVQQKDBhJbnRlcm5ldCBXaWRnaXRzIFB0eSBM\n" + 
		    "dGQxFTATBgNVBAMMDDU5NzAzNTE2NTI1ODCCASIwDQYJKoZIhvcNAQEBBQADggEP\n" + 
		    "ADCCAQoCggEBAJwyo2dPddvAIivWs4Y9tmIZyoFF28Xo97lWTIy+mekVXwjZzsXh\n" + 
		    "IETYuL0j0YuIVwtZwb0I7tHsykI6i+dgEPyvWEDIWg8aVlHyeLXjCEO9yh0/V8vW\n" + 
		    "uFRqGekCQabzDcLsF1eZCRer0n7codnA5ehjz8AZJDHIKH5eE7Cuh4EWYbrcCgQ1\n" + 
		    "DQ3xoOSQFwmRmYbePDiHg8fNOWhrO6sTDPZn/eFigFv5VK0F7NibNTdPCOP/AR9E\n" + 
		    "lxQ/2dBFhB2d1BAmatAzkbo3ML88h9z0+WiRshuzRtSIQzA3xTCt0uGDVr2/bBAg\n" + 
		    "fyuCNAmnN7QFWkvZnJMzTM8CG52HGaNEdW0CAwEAATANBgkqhkiG9w0BAQsFAAOC\n" + 
		    "AQEAWJVGAb+h1M11shu/vpXDLrSgZxRBBNF2QyasaJ/BeLgwhx9sw+k+9kp+nCWo\n" + 
		    "IwBiyU99qapPEfV/7kQfoTGEeDXfOXenS6mmfwIIz02m6N8OGxwD0xTpD9kOJn/s\n" + 
		    "SYfCoNgydoFiiqSv8YfDMlNVln2VDivMy5BhZWMg/donbWnHiFvr7KkMp2mRnx3X\n" + 
		    "UZnvLkanuRAXYcsfd6TxB0Qp1jdJiyNK4qS7g0vVNA1AJoEVExkNwCHc+jFgwlhx\n" + 
		    "pmiOrpZi8BNCXozRVklHRAug1BUkU8oebs4lG4GRlKve/hodXIYFim4kYLBOf62d\n" + 
		    "KsRm56fi6yPjwszOw+mZ4uOMPw==\n" + 
		    "-----END CERTIFICATE-----\n");
		return configuration;
	}
	
	public ResponseEntity<GenericResponse> createRequest(UsuarioOneClick user) throws Exception {
		
		//String urlReturn = "https://pikapp-8be64.appspot.com/oneClick/confirmRegister";
		String urlReturn = "http://localhost:8080/oneClick/confirmRegister";
		GenericResponse genericResponse = new GenericResponse();
		HttpStatus status = null;
		Map<String, Object> details = new HashMap<>();
		Configuration configuration = new Configuration();
		try {
			configuration = generarConfiguracionTransBank();
			WebpayOneClick transaction = new Webpay(configuration.forTestingWebpayOneClickNormal()).getOneClickTransaction();
			OneClickInscriptionOutput initResult = transaction.initInscription(user.getUserName(), user.getEmail(), urlReturn);
			
			String tokenWs = initResult.getToken();
			String urlWebpay = initResult.getUrlWebpay();
			
			if(!urlWebpay.isEmpty()) {
				details.put("url", urlWebpay);
				details.put("TBK_TOKEN", tokenWs);
				
				genericResponse.setCode(200);
				genericResponse.setMsg("Transaccion exitosa!");
				genericResponse.setResponse(details);
				status = HttpStatus.OK;
			}else {
				genericResponse.setCode(500);
				genericResponse.setMsg("Transaccion no terminada!");
				status = HttpStatus.NO_CONTENT;
			}
			
			
		} catch (Exception e) {
			System.out.println("Inscripcion fallida");
			System.out.println(e);
			genericResponse.setCode(500);
			genericResponse.setMsg("Inscripcion Fallida!");
			status = HttpStatus.NO_CONTENT;
		}
		
		
		return new ResponseEntity<>(genericResponse, status);
	}
	
	public void confirmRegisterUser(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws Exception {
		GenericResponse genericResponse = new GenericResponse();
		HttpStatus status = null;
		Map<String, Object> details = new HashMap<>();
		String token = httpRequest.getParameter("TBK_TOKEN");
		Configuration configuration = new Configuration();
		try {
			configuration = generarConfiguracionTransBank();
			WebpayOneClick transaction = new Webpay(configuration.forTestingWebpayOneClickNormal()).getOneClickTransaction();
			OneClickFinishInscriptionOutput result = transaction.finishInscription(token);
			
			int responseCode = result.getResponseCode();
			String Authcode = result.getAuthCode();
			String cardType = result.getCreditCardType().value();
			String cardDigits = result.getLast4CardDigits();
			String tbkUser = result.getTbkUser();
			
			if(responseCode == 0) {
				
				details.put("Authcode", Authcode);
				details.put("cardType", cardType);
				details.put("cardDigits", cardDigits);
				details.put("tbkUser", tbkUser);
				
				genericResponse.setCode(200);
				genericResponse.setMsg("Transaccion exitosa!");
				genericResponse.setResponse(details);
				status = HttpStatus.OK;
				
				httpResponse.setHeader("Content-Type", "application/x-www-form-urlencoded");
				httpResponse.setStatus(307);
				httpResponse.setHeader("Location", "http://localhost:8101/transaccionExito="+Authcode+":"+cardType+":"+cardDigits+":"+tbkUser);
				
			}else {
				genericResponse.setCode(500);
				genericResponse.setMsg("Transaccion no terminada!");
				status = HttpStatus.NO_CONTENT;
				System.out.println("Transaccion anulada");
				httpResponse.setHeader("Content-Type", "application/x-www-form-urlencoded");	 
				httpResponse.setStatus(307);
				httpResponse.setHeader("Location", "http://localhost:8101/transaccionRechazada");
			}
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		//return new ResponseEntity<>(genericResponse, status);
	}
	
	public ResponseEntity<GenericResponse> removerUsuario(CompraOneClick compra) throws Exception {
		GenericResponse genericResponse = new GenericResponse();
		HttpStatus status = HttpStatus.NO_CONTENT;
		Map<String, Object> details = new HashMap<>();
		Configuration configuration = new Configuration();
		try {
			configuration = generarConfiguracionTransBank();
			WebpayOneClick transaction = new Webpay(configuration.forTestingWebpayOneClickNormal()).getOneClickTransaction();
			boolean success = transaction.removeUser(compra.getTbkUser(), compra.getUserName());
			if (success) {
				details.put("estadoSolicitud", "Usuario removido exitosamente!");
				genericResponse.setCode(200);
				genericResponse.setMsg("Transaccion exitosa!");
				genericResponse.setResponse(details);
				status = HttpStatus.OK;
			}else if(!success){
				details.put("estadoSolicitud", "Usuario no fue removido");
				genericResponse.setCode(200);
				genericResponse.setMsg("Transaccion exitosa!");
				genericResponse.setResponse(details);
				status = HttpStatus.OK;
			}
			
		} catch (Exception e) {
			System.out.println("remover fallida");
			System.out.println(e);
			genericResponse.setCode(500);
			genericResponse.setMsg("remover Fallida!");
			status = HttpStatus.NO_CONTENT;
		}
		return  new ResponseEntity<>(genericResponse, status);
		
	}
	
	public ResponseEntity<GenericResponse> realizaPago(CompraOneClick compra) throws Exception {
		GenericResponse genericResponse = new GenericResponse();
		HttpStatus status = HttpStatus.NO_CONTENT;
		Map<String, Object> details = new HashMap<>();
		Configuration configuration = new Configuration();
		try {
			FirebaseWebPayService fire = new FirebaseWebPayService(FirebaseWebPayService.inicializarFirestorePikapp(),
					FirebaseWebPayService.PIKAPP);
			
			configuration = generarConfiguracionTransBank();
			WebpayOneClick transaction = new Webpay(Configuration.forTestingWebpayOneClickNormal()).getOneClickTransaction();
			OneClickPayOutput output = transaction.authorize(compra.getBuyOrder(), compra.getTbkUser(), compra.getUserName(), compra.getAmount());
			if (output.getResponseCode() == 0) {
				File file = QRCode.from(String.valueOf(output.getTransactionId())).to(ImageType.PNG).withSize(300, 300).file();
				String base64 = Util.encoder(file.getAbsolutePath());
				details.put("authorizationCode", output.getAuthorizationCode());
				details.put("creditCardType", output.getCreditCardType().value());
				details.put("last4CardDigits", output.getLast4CardDigits());
				details.put("transactionId", output.getTransactionId());
				details.put("responseCode", output.getResponseCode());
				details.put("qr", base64);
				
				fire.updateBilling(output, compra, base64);
				
				genericResponse.setCode(200);
				genericResponse.setMsg("Transaccion exitosa!");
				genericResponse.setResponse(details);
				status = HttpStatus.OK;
			}else {
				genericResponse.setCode(500);
				genericResponse.setMsg("Transaccion Fallida!");
				status = HttpStatus.NO_CONTENT;
			}
			
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Transaccion anulada");
			System.out.println(e);
			genericResponse.setCode(500);
			genericResponse.setMsg("Transaccion Fallida!");
			status = HttpStatus.NO_CONTENT;
		}
		
		return new ResponseEntity<>(genericResponse, status);
	}
	
	public ResponseEntity<GenericResponse> reversarPago(CompraOneClick compra) throws Exception {
		GenericResponse genericResponse = new GenericResponse();
		HttpStatus status = HttpStatus.NO_CONTENT;
		Map<String, Object> details = new HashMap<>();
		Configuration configuration = new Configuration();
		try {
			configuration = generarConfiguracionTransBank();
			WebpayOneClick transaction = new Webpay(configuration.forTestingWebpayOneClickNormal()).getOneClickTransaction();
			OneClickReverseInput input = new OneClickReverseInput();
			input.setBuyorder(compra.getBuyOrder());
			OneClickReverseOutput success = transaction.codeReverseOneClick(input);
			
			if (success.isReversed()) {
				details.put("estadoSolicitud", "Transaccion reversada exitosamente!");
				genericResponse.setCode(200);
				genericResponse.setMsg("Transaccion exitosa!");
				genericResponse.setResponse(details);
				status = HttpStatus.OK;
			}else if(!success.isReversed()){
				details.put("estadoSolicitud", "Transaccion no fue reversada");
				genericResponse.setCode(200);
				genericResponse.setMsg("Transaccion no realizada!");
				genericResponse.setResponse(details);
				status = HttpStatus.OK;
			}
			
		} catch (Exception e) {
			System.out.println("Reversa anulada");
			System.out.println(e);
			genericResponse.setCode(500);
			genericResponse.setMsg("Reversa Fallida!");
			status = HttpStatus.NO_CONTENT;
		}
		return new ResponseEntity<>(genericResponse, status);
	}

}
