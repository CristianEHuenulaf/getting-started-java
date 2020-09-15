package com.example.appengine.demos.springboot.services;

import com.example.appengine.demos.springboot.model.Billing;
import com.example.appengine.demos.springboot.model.CompraOneClick;
import com.example.appengine.demos.springboot.model.ImageDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import com.google.cloud.storage.Acl;
import com.google.cloud.storage.Acl.Role;
import com.google.cloud.storage.Acl.User;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.xml.bind.DatatypeConverter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.ResourceUtils;
import org.threeten.bp.LocalDateTime;

public class FirebaseWebPayService {

	private static Firestore dbPikapp = null;
	public static Storage storage = null;
	
	private static final Logger logger = LogManager.getLogger(FirebaseWebPayService.class);

	public FirebaseWebPayService(Firestore db) {
			FirebaseWebPayService.dbPikapp = db;
	}

	public static Storage initializerStorage() {
		if (FirebaseWebPayService.storage == null) {
			GoogleCredentials credentials;
			try {
				File file = ResourceUtils.getFile("classpath:merci-db.json");
				// File file =
				// ResourceUtils.getFile("classpath:pikapp-dev-269319-firebase-adminsdk-4zd99-15fc2d73c8.json");
				FileInputStream serviceAccount = new FileInputStream(file);

				credentials = GoogleCredentials.fromStream(serviceAccount);
				System.out.println("Credentials OK");
			} catch (IOException e) {
				System.out.println("Error while reading Firebase config file." + e);
				throw new IllegalStateException(e);
			}
			FirebaseWebPayService.storage = StorageOptions.newBuilder().setCredentials(credentials).build()
					.getService();
			return FirebaseWebPayService.storage;
		} else {
			return FirebaseWebPayService.storage;
		}

	}

	public static Firestore initializerFirestoreToken() {
		if (FirebaseWebPayService.dbPikapp == null) {
			final GoogleCredentials credentials;
			try {
				// File file = ResourceUtils.getFile("classpath:pikapp-ventas-firebase-adminsdk-c4e0i-efcc603ecc.json");
				File file = ResourceUtils.getFile("classpath:merci-db.json");
				final FileInputStream serviceAccount = new FileInputStream(file);

				credentials = GoogleCredentials.fromStream(serviceAccount);
				System.out.println("Credentials OK");
			} catch (IOException e) {
				System.out.println("Error while reading Firebase config file." + e);
				throw new IllegalStateException(e);
			}
			final FirebaseOptions options = new FirebaseOptions.Builder()
					.setDatabaseUrl("https://merci-app-3c551.firebaseio.com").setCredentials(credentials).build();
			FirebaseApp.initializeApp(options);
			Firestore firestore = FirestoreClient.getFirestore(FirebaseApp.initializeApp(options, "merci-app-3c551"));
			return firestore;
		} else {
			return FirebaseWebPayService.dbPikapp;
		}

	}

	public void saveBilling(String idBilling, String token, String id) throws InterruptedException, ExecutionException {
		// Create a Map to store the data we want to set
		Map<String, Object> docData = new HashMap<>();
		docData.put("transactionId", idBilling);
		docData.put("token", token);
		CollectionReference billing = dbPikapp.collection("billing");
		List<ApiFuture<WriteResult>> futures = new ArrayList<>();
		futures.add(billing.document(id).set(docData));

		ApiFutures.allAsList(futures);
		
	}

	public Billing findBillingByToken(String token) throws Exception {
		Billing bill = new Billing();
		ObjectMapper oMapper = new ObjectMapper();
		// [START fs_get_doc_as_map]
		CollectionReference billing = dbPikapp.collection("billing");
		Query query = billing.whereEqualTo("token", token);
		// retrieve query results asynchronously using query.get()
		ApiFuture<QuerySnapshot> querySnapshot = query.get();
		System.out.println(querySnapshot.get().getDocuments());
		for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
			System.out.println(document.getId());
			if (document.exists()) {
				bill = document.toObject(Billing.class);
				System.out.println("Document data: " + document.getData());
				System.out.println("Billing data: " + bill.toString());
			} else {
				System.out.println("No such document!");
			}
		}
		// [END fs_get_doc_as_map]
		return bill;
	}

	/*
	 * public void updateBilling(OneClickPayOutput respuestaTransbank,
	 * CompraOneClick compra, String base64) { Map<String, Object> billing = new
	 * HashMap<>();
	 * 
	 * billing.put("status", 2); billing.put("qrPurchase", base64);
	 * billing.put("transactionId", respuestaTransbank.getTransactionId());
	 * billing.put("payFormat", respuestaTransbank.getCreditCardType().value());
	 * billing.put("last4CardDigits", respuestaTransbank.getLast4CardDigits());
	 * billing.put("transBankResponse", respuestaTransbank.toString());
	 * 
	 * DocumentReference compraPikapp =
	 * dbPikapp.collection("compras").document(compra.getSessionId()); //
	 * dbPikapp.collection("compras").document(compra.getSessionId()).update(billing
	 * ); ApiFuture<WriteResult> result = compraPikapp.update(billing); }
	 */
	
	public ImageDTO saveImage(byte[] image, File png, String imgName, String store) {
		Credentials credentials;
		String imgUrl = "";
		ImageDTO imageDTO = new ImageDTO();
		try {
			File file = ResourceUtils.getFile("classpath:merci-db.json");
			// File file =
			// ResourceUtils.getFile("classpath:pikapp-dev-269319-firebase-adminsdk-4zd99-15fc2d73c8.json");
			FileInputStream serviceAccount = new FileInputStream(file);
		//	credentials = GoogleCredentials.fromStream(new FileInputStream("C:\\Users\\carlo\\Desktop\\pikapp-dev.json"));
			credentials = GoogleCredentials.fromStream(serviceAccount);
			Storage storage = StorageOptions.newBuilder().setCredentials(credentials).setProjectId("merci-app-3c551")
					.build().getService();
			//Bucket bucket = storage.get("merci-app-3c551.appspot.com");
		    
			//imgUrl = bucket.create("pikapp/QR/"+imgName, image).getMediaLink();
			//imgUrl = imgUrl.replace("https://www.googleapis.com/download/storage/v1/",
			//		"https://firebasestorage.googleapis.com/v0/");
		    // Upload a local file to a new file to be created in your bucket.
		    InputStream uploadContent = new DataInputStream(new FileInputStream(png));
		    BlobId blobId = BlobId.of("merci-app-3c551.appspot.com", "pikapp/QR/"+imgName);
		    BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("image/png").build();
		    Blob zebraBlob = storage.create(blobInfo, image);
		    storage.createAcl(blobId, Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER));
		    imgUrl = zebraBlob.getMediaLink();
		    imgUrl = imgUrl.replace("https://storage.cloud.google.com/merci-app-3c551.appspot.com/",
									"https://storage.googleapis.com/merci-app-3c551.appspot.com");
		    
			imageDTO.setProductImageUrl(imgUrl);
			imageDTO.setProductImageUrlPath(imgUrl);
		} catch (IOException e) {
			logger.error("Error al encontrar key del proyecto");
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return imageDTO;
	}

//	private String getImagePath(String uri) {
//		uri = uri.split("\\?generation")[0];
//		String name = "";
//		try {
//			URL url = new URL(uri);
//			URLConnection request = url.openConnection();
//			request.connect();			 
//			JsonElement root = JsonParser.parseReader(new InputStreamReader((InputStream) request.getContent()));
//			JsonObject rootobj = root.getAsJsonObject(); 
//			name = rootobj.get("name").getAsString(); 
//		} catch (Exception e) {
//			logger.error(e.getMessage());
//		}
//		return name;
//	}


}
