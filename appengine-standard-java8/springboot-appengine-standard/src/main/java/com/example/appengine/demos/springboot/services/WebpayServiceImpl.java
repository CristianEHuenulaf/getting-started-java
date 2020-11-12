package com.example.appengine.demos.springboot.services;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.appengine.demos.springboot.config.GenericResponse;
import com.example.appengine.demos.springboot.model.Billing;
import com.example.appengine.demos.springboot.model.Compra;
import com.example.appengine.demos.springboot.model.TransactionResult;
import com.example.appengine.demos.springboot.util.Util;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.*;
import com.transbank.webpay.wswebpay.service.TransactionResultOutput;
import com.transbank.webpay.wswebpay.service.WsInitTransactionOutput;
import com.transbank.webpay.wswebpay.service.WsTransactionDetailOutput;

import cl.transbank.onepay.exception.TransactionCreateException;
import cl.transbank.webpay.Webpay;
import cl.transbank.webpay.WebpayNormal;
import cl.transbank.webpay.configuration.Configuration;

@Service
public class WebpayServiceImpl implements WebpayService {

	Util util = new Util();
	
	private static final Logger logger = LogManager.getLogger(WebpayServiceImpl.class);
	
	private Configuration generarConfiguracionTransBank() {
		Configuration configuration = new Configuration();
		configuration.setEnvironment(Webpay.Environment.PRODUCCION);
		// a continuación va tu código de comercio, Si el código que posees es de 8 dígitos debes anteponer 5970.
		configuration.setCommerceCode("597035010378");
		configuration.setPrivateKey( // pega acá la llave privada de tu certificado
		    "-----BEGIN RSA PRIVATE KEY-----\r\n" + 
		    "MIIEpAIBAAKCAQEA05DrgkjuvPoPBqRFqRznkyvnMs05n1sdnkLpsK17aRtTPBq8\r\n" + 
		    "8r9hRUq37wTB2f5wVTYEkyWsGfkbCx7peBU5KS05WAC0+Igg87jluQFuZbUbg74m\r\n" + 
		    "vlRw8S1967jKmWL1QxucvWsmzRr9C0bK774AYT2Ucr3EFfqkYynxGKtRZkMXaxjB\r\n" + 
		    "8CH1+ldNw7PV08PrXnqe4tn7fLzXY51z8yG+uWpdtj1sNhHsZLjWaRI5252A+tpW\r\n" + 
		    "veoQORVztY/B1fY9e1KqL1LFRLJM5FgX55jRS3xTezeM9orlWCUMEV6eg0XrRg0d\r\n" + 
		    "XC6V2/OvZ6Z6h27SoWDwa31osbv6s7T2xbIeVwIDAQABAoIBAFUkwpMZVPM606dB\r\n" + 
		    "n0NS/aOODKq2SxVl7wKNPU08VClVtObAgABVowjm7CVGTdm0e3LfnXG2vI9rYEXW\r\n" + 
		    "d/E64Vk/+Q8AodRPvKs5Y0JUYCCQhXU8poz8oMN/ImkmYmrel4QdbbQRlnj+/o12\r\n" + 
		    "PR/E4JRB2ydeVbbeux+pdn0OHWFArYcChGgldnHQGmlQ4fojrtLocimaJ1wHE9bg\r\n" + 
		    "RObEaphjPhJ5agTByc7+++BvI35JeOEvuSIMzmRND5IeSKEFVQIXELaEg4f7tny+\r\n" + 
		    "Ii1p6hs4eHsu1cavd88HUBhuDQbNSAY90ZYt4Cic1XALWUElQoXuEj3DLXRAzuoU\r\n" + 
		    "YUs4YcECgYEA8mwvznlgYCZ7pS1FfcO+l3emne/TwjbRo0AiSwlNSvADeqXQtIHQ\r\n" + 
		    "mUNerbuTdWfnp0DFJasT2PwI38yl69lexczBjkpUBGXJeLTRUAbQNgKynPnLA4gA\r\n" + 
		    "KurBYrFCvhUWVPtXB/L/onGCjrqFqZbXRxjL03YZyrSWEVlbZOo8WmECgYEA32pR\r\n" + 
		    "YqG+/Ox6LWmQ+QUG638YhNEWqBinmf7ZV0KNAuzmHt3XVcHxVykSEo2mLNPygK+V\r\n" + 
		    "pk6KO03ngt0zEkJNPAiPx/gpoAK0FLyl5kW/CABxPARlVdQsOWh/CCOg3bbQaP9x\r\n" + 
		    "BqXXqPl40vrKDMYECLZIc8MqVsfAf/vi/GfaY7cCgYEAw2eFGW8oyhf67meRbhBM\r\n" + 
		    "HpdpjED2dcPuMyLNAaLb1J2mkEP7+KPy2rc9J9jcwXQhe+VvhC9j7jEpjgvNalBq\r\n" + 
		    "XhHvqpwrIOhS/6LwXQtA5WhDRNjsVUpYqD9V5hFv9PvWZmW5/0RV3kCUtiuO9eYK\r\n" + 
		    "XNqV4Tt/Cq+Jjy5xxuQwBcECgYEAtywP9fHvCKdJtJnsxn4cok/d5mcXUFKGd3e+\r\n" + 
		    "8hAaVN+t/Hid6C3OlUuTCashJ3fORzuuUl/qbZz9xwlcqge6ExJh7+3DEvGke6v/\r\n" + 
		    "yMA+U20paa+g9bGzYI4QOLgdWCpezPdBz9WA74U6TqBXsAlZnVr2JBNLT0MOzN/T\r\n" + 
		    "xmXPZBkCgYBdJdX35JFyAg+YLFlUY13Ci5AVy0VjGIeouWD4LiBhnF+E66LRXp8D\r\n" + 
		    "Pe4k5s1DritiT8JmzJdMT8RSldxK4VDnbBUWOqElUYSKqcopm2taMPwjZ6FsJETW\r\n" + 
		    "gwcHVDkK1mPvLkTLMSTrSs7zjCm6YkEWXcfAVzSXus9Yoln3umC3kw==\r\n" + 
		    "-----END RSA PRIVATE KEY-----\r\n" + 
		    "");
		configuration.setPublicCert( // pega acá tu certificado público
		    "-----BEGIN CERTIFICATE-----\r\n" + 
		    "MIIDPzCCAicCFASvIweWzN+4hWvgWJdEBjVxBfPZMA0GCSqGSIb3DQEBCwUAMFwx\r\n" + 
		    "CzAJBgNVBAYTAkFVMRMwEQYDVQQIDApTb21lLVN0YXRlMSEwHwYDVQQKDBhJbnRl\r\n" + 
		    "cm5ldCBXaWRnaXRzIFB0eSBMdGQxFTATBgNVBAMMDDU5NzAzNTAxMDM3ODAeFw0y\r\n" + 
		    "MDAyMjQxODI2NDFaFw0yNDAyMjMxODI2NDFaMFwxCzAJBgNVBAYTAkFVMRMwEQYD\r\n" + 
		    "VQQIDApTb21lLVN0YXRlMSEwHwYDVQQKDBhJbnRlcm5ldCBXaWRnaXRzIFB0eSBM\r\n" + 
		    "dGQxFTATBgNVBAMMDDU5NzAzNTAxMDM3ODCCASIwDQYJKoZIhvcNAQEBBQADggEP\r\n" + 
		    "ADCCAQoCggEBANOQ64JI7rz6DwakRakc55Mr5zLNOZ9bHZ5C6bCte2kbUzwavPK/\r\n" + 
		    "YUVKt+8Ewdn+cFU2BJMlrBn5Gwse6XgVOSktOVgAtPiIIPO45bkBbmW1G4O+Jr5U\r\n" + 
		    "cPEtfeu4ypli9UMbnL1rJs0a/QtGyu++AGE9lHK9xBX6pGMp8RirUWZDF2sYwfAh\r\n" + 
		    "9fpXTcOz1dPD6156nuLZ+3y812Odc/MhvrlqXbY9bDYR7GS41mkSOdudgPraVr3q\r\n" + 
		    "EDkVc7WPwdX2PXtSqi9SxUSyTORYF+eY0Ut8U3s3jPaK5VglDBFenoNF60YNHVwu\r\n" + 
		    "ldvzr2emeodu0qFg8Gt9aLG7+rO09sWyHlcCAwEAATANBgkqhkiG9w0BAQsFAAOC\r\n" + 
		    "AQEAL6yKpgxLJcX/svyFWuLBGuzHkIdFXXt5zaFka6LLfOAvKF2kjwQMs2bGlDB6\r\n" + 
		    "zTbuiE8y23rZb5GelWUt7Z3psdyvQ5tOvV+25RvblWTYzhi58c1AoI8jomlqxxUj\r\n" + 
		    "IECa6nw0LvsR3JdbOos34HGNv8g3/nbWQ98hO8alwsSY3x18gZ733kj7ohQ16o2u\r\n" + 
		    "AdlKoTZuPE13iA26v7o15khAyFKrMmLQwMqTc/R7ONOhtCX4zQje1iBp/QOFqxBt\r\n" + 
		    "xgzfNdA+R01ruZ4IkjvkJ7nHf3OoSsKE4S00nUDLWn5NySoH0sqVs5Nj6nZCMMye\r\n" + 
		    "R8hYWnYpT/i61Pltmg7lxposNw==\r\n" + 
		    "-----END CERTIFICATE-----");
		return configuration;
	}
	
	
	public ResponseEntity<GenericResponse> createRequest(Compra compra) throws Exception {
		
		GenericResponse genericResponse = new GenericResponse();
		HttpStatus status = null;
		
		String buyOrder = compra.getBuyOrder();
		String sessionId = compra.getSessionId();
        double amount = compra.getAmount();
        String finalUrl = compra.getUrlRedirect();
        String billingId = compra.getBillingId();
		
        Map<String, Object> details = new HashMap<>();
        details.put("buyOrder", buyOrder);
        details.put("sessionId", sessionId);
        details.put("amount", amount);
        details.put("billingId", billingId);
      
		Configuration configuration = new Configuration();
		try {
			configuration = generarConfiguracionTransBank();
			WebpayNormal transaction = new Webpay(configuration).getNormalTransaction();
			
			WsInitTransactionOutput initResult = transaction.initTransaction(amount, sessionId, buyOrder,"https://pikapp-8be64.appspot.com/webpay-result", finalUrl);	
			
			ObjectMapper mapper = new ObjectMapper();
			// Converting the Object to JSONString
			String jsonString = mapper.writeValueAsString(initResult);
			
			
			String formAction = initResult.getUrl();
			String tokenWs = initResult.getToken();
					
			details.put("url", formAction);
			details.put("token", tokenWs);
			Billing billing = new Billing();
			
			if(!formAction.isEmpty()) {
				billing.setTransactionId(billingId);
				billing.setToken(tokenWs);
				genericResponse.setCode(200);
				genericResponse.setMsg("Transaccion exitosa!");
				genericResponse.setResponse(details);
				status = HttpStatus.OK;
				FirebaseWebPayService firebaseWebpay = new FirebaseWebPayService(FirebaseWebPayService.initializerFirestoreToken());
				firebaseWebpay.saveBilling(buyOrder, tokenWs, billingId);
				
			}else {
				genericResponse.setCode(500);
				genericResponse.setMsg("Transaccion exitosa!");
				status = HttpStatus.NO_CONTENT;
			}
						
			
		} catch (TransactionCreateException e ) {
			e.printStackTrace();
		}
		
		return new ResponseEntity<>(genericResponse,status);
	}
	
	public ResponseEntity<GenericResponse> createRequestNew(Compra compra) throws Exception {
		
		GenericResponse genericResponse = new GenericResponse();
		HttpStatus status = null;
		
		String buyOrder = compra.getBuyOrder();
		String sessionId = compra.getSessionId();
        double amount = compra.getAmount();
        String finalUrl = compra.getUrlRedirect();
        String billingId = compra.getBillingId();
		
        Map<String, Object> details = new HashMap<>();
        details.put("buyOrder", buyOrder);
        details.put("sessionId", sessionId);
        details.put("amount", amount);
        details.put("billingId", billingId);
      
		Configuration configuration = new Configuration();
		try {
			configuration = generarConfiguracionTransBank();
			WebpayNormal transaction = new Webpay(configuration).getNormalTransaction();
			
			WsInitTransactionOutput initResult = transaction.initTransaction(amount, sessionId, buyOrder,"https://pikapp-8be64.appspot.com/webpay-result", finalUrl);	
			
			ObjectMapper mapper = new ObjectMapper();
			// Converting the Object to JSONString
			String jsonString = mapper.writeValueAsString(initResult);
			
			
			String formAction = initResult.getUrl();
			String tokenWs = initResult.getToken();
					
			details.put("url", formAction);
			details.put("token", tokenWs);
			Billing billing = new Billing();
			
			if(!formAction.isEmpty()) {
				billing.setTransactionId(billingId);
				billing.setToken(tokenWs);
				genericResponse.setCode(200);
				genericResponse.setMsg("Transaccion exitosa!");
				genericResponse.setResponse(details);
				status = HttpStatus.OK;
				FirebaseWebPayService firebaseWebpay = new FirebaseWebPayService(FirebaseWebPayService.initializerFirestoreToken());
				firebaseWebpay.saveBilling(buyOrder, tokenWs, compra.getIdCompra());
				
			}else {
				genericResponse.setCode(500);
				genericResponse.setMsg("Transaccion exitosa!");
				status = HttpStatus.NO_CONTENT;
			}
						
			
		} catch (TransactionCreateException e ) {
			e.printStackTrace();
		}
		
		return new ResponseEntity<>(genericResponse,status);
	}

	
	public void validateTransaction(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
		String token = httpRequest.getParameter("token_ws");
		Configuration configuration = new Configuration();
		try {
			configuration = generarConfiguracionTransBank();
			WebpayNormal transaction = new Webpay(configuration).getNormalTransaction();
			TransactionResultOutput result = transaction.getTransactionResult(token);	
			WsTransactionDetailOutput output = result.getDetailOutput().get(0);
			TransactionResult transactionResult = new TransactionResult();
						
			if(output.getResponseCode() == 0) {
				transactionResult.setBuyOrder(result.getBuyOrder());
				transactionResult.setSessionId(result.getSessionId());
				transactionResult.setCardNumber(result.getCardDetail().getCardNumber());
				transactionResult.setCardExpirationDate(result.getCardDetail().getCardExpirationDate());
				transactionResult.setAccoutingDate(result.getAccountingDate());
				transactionResult.setTransactionDate(result.getTransactionDate());
				transactionResult.setVci(result.getVCI());
				transactionResult.setUrlRedirect(result.getUrlRedirection());
				
				//FirebaseWebPayService firebaseWebpay = new FirebaseWebPayService(FirebaseWebPayService.initializerFirestoreToken());
				//Billing bill = firebaseWebpay.findBillingByToken(token);
				//bill.setStatus(2);
				//getQr(token);
				
				String url =  "http://localhost:8080/ZXhpdG8="+result.getBuyOrder()+","+output.getAuthorizationCode();
				httpResponse.setHeader("Content-Type", "application/x-www-form-urlencoded");
				httpResponse.setStatus(307);
				httpResponse.setHeader("Location", url);

			}else {
				System.out.println("Transaccion anulada");
				httpResponse.setHeader("Content-Type", "application/x-www-form-urlencoded");	 
				httpResponse.setStatus(307);
				httpResponse.setHeader("Location", "http://localhost:8080/transaccionRechazada");
		
			}
				
		}catch(Exception ex) {
				
			System.out.println("Error: "+ex.getLocalizedMessage());
			ex.printStackTrace();
		}

	}
	
	public void validateTransactionNew(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
		String token = httpRequest.getParameter("token_ws");
		Configuration configuration = new Configuration();
		try {
			configuration = generarConfiguracionTransBank();
			WebpayNormal transaction = new Webpay(configuration).getNormalTransaction();
			TransactionResultOutput result = transaction.getTransactionResult(token);	
			WsTransactionDetailOutput output = result.getDetailOutput().get(0);
			TransactionResult transactionResult = new TransactionResult();
	        ObjectMapper oMapper = new ObjectMapper();
	    
	        Map<String, String> mapResult = oMapper.convertValue(result, Map.class);
	        mapResult.remove("urlRedirection");
	        mapResult.remove("cardExpirationDate");
	        mapResult.remove("transactionDate");
	        mapResult.remove("detailOutput");
	        mapResult.remove("cardDetail");
	        mapResult.put("responseCode", String.valueOf(result.getDetailOutput().get(0).getResponseCode()));
			if(output.getResponseCode() == 0) {
				mapResult.put("cardNumber", result.getCardDetail().getCardNumber());
				mapResult.put("paymentTypeCode", result.getDetailOutput().get(0).getPaymentTypeCode());
		        mapResult.put("cardExpirationDate", result.getCardDetail().getCardExpirationDate());
		        mapResult.put("buyOrder", result.getDetailOutput().get(0).getBuyOrder());
		        mapResult.put("authorizationCode", result.getDetailOutput().get(0).getAuthorizationCode());

		        
		        System.out.println("prueba >>"+mapResult);
		        
				FirebaseWebPayService firebaseWebpay = new FirebaseWebPayService(FirebaseWebPayService.initializerFirestoreToken());
				Billing bill = firebaseWebpay.findBillingByTokenNew(token);
				firebaseWebpay.saveBilling(bill.getId(), mapResult);
		        
				transactionResult.setBuyOrder(result.getBuyOrder());
				transactionResult.setSessionId(result.getSessionId());
				transactionResult.setCardNumber(result.getCardDetail().getCardNumber());
				transactionResult.setCardExpirationDate(result.getCardDetail().getCardExpirationDate());
				transactionResult.setAccoutingDate(result.getAccountingDate());
				transactionResult.setTransactionDate(result.getTransactionDate());
				transactionResult.setVci(result.getVCI());
				transactionResult.setUrlRedirect(result.getUrlRedirection());
				//bill.setStatus(2);
				//getQr(token);
				
				String url =  "http://localhost:8080/ZXhpdG8="+result.getBuyOrder()+","+output.getAuthorizationCode();
				httpResponse.setHeader("Content-Type", "application/x-www-form-urlencoded");
				httpResponse.setStatus(307);
				httpResponse.setHeader("Location", url);

			}else {
				FirebaseWebPayService firebaseWebpay = new FirebaseWebPayService(FirebaseWebPayService.initializerFirestoreToken());
				Billing bill = firebaseWebpay.findBillingByToken(token);
				firebaseWebpay.saveBilling(bill.getTransactionId(), mapResult);
				System.out.println("Transaccion anulada");
				httpResponse.setHeader("Content-Type", "application/x-www-form-urlencoded");	 
				httpResponse.setStatus(307);
				httpResponse.setHeader("Location", "http://localhost:8080/transaccionRechazada");
		
			}
				
		}catch(Exception ex) {
				
			System.out.println("Error: "+ex.getLocalizedMessage());
			ex.printStackTrace();
		}

	}
	
	public ResponseEntity<GenericResponse> getQr(String token){
		Map<String, Object> details = new HashMap<>();
		GenericResponse genericResponse = new GenericResponse();
		HttpStatus status = null;
		try {			
			JsonObject json = new Gson().fromJson(token, JsonObject.class);
			String finalToken = json.get("token").getAsString();
			FirebaseWebPayService firebaseWebpay = new FirebaseWebPayService(FirebaseWebPayService.initializerFirestoreToken());
			Billing billing = firebaseWebpay.findBillingByToken(finalToken);
			
			File file = QRCode.from(billing.getTransactionId()).to(ImageType.PNG).withSize(300, 300).file();
			String base64 = Util.encoder(file.getAbsolutePath());
			byte[] fileContent = readFileToByteArray(file);
			
			//firebaseWebpay.saveImage(fileContent, file, billing.getTransactionId(), billing.getIdStore());
			if(file.exists()) {
				file.delete();
			}
		
			if(base64 != "") {
				details.put("base64", base64);
				status = HttpStatus.CREATED;
				genericResponse.setCode(201);
				genericResponse.setMsg("Codigo QR creado");
				genericResponse.setResponse(base64);
			}else {
				status = HttpStatus.NO_CONTENT;
				genericResponse.setCode(204);
				genericResponse.setMsg("Error al generar codigo QR");
				genericResponse.setResponse(null);
			}
	
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
		
		return new ResponseEntity<>(genericResponse, status );
	}
	
	public byte[] fileToByteArray(String imgSource) {
		File file = new File(imgSource);
		byte[] bArray = readFileToByteArray(file);
		return bArray;
	}

	private static byte[] readFileToByteArray(File file) {
		FileInputStream fis = null;
		byte[] bArray = new byte[(int) file.length()];
		try {
			fis = new FileInputStream(file);
			fis.read(bArray);
			fis.close();
		} catch (IOException ioExp) {
			ioExp.printStackTrace();
		}
		return bArray;
	}
}
