package com.example.appengine.demos.springboot.services;

import com.example.appengine.demos.springboot.model.Billing;
import com.example.appengine.demos.springboot.model.CompraOneClick;
import com.google.api.core.ApiFuture;
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
import com.transbank.webpayserver.webservices.OneClickPayOutput;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.springframework.util.ResourceUtils;

public class FirebaseWebPayService {

	private static Firestore dbToken = null;
	private static Firestore dbPikapp = null;

	public static final int PIKAPP = 0;
	public static final int TOKEN = 1;

	public FirebaseWebPayService(Firestore db, int destinoBD) {
		if (destinoBD == 0) {
			FirebaseWebPayService.dbPikapp = db;
		}
		if (destinoBD == 1) {
			FirebaseWebPayService.dbToken = db;
		}
	}

	public static Firestore initializerFirestoreToken() {
		if (FirebaseWebPayService.dbToken == null) {
			final GoogleCredentials credentials;
			try {
				//File file = ResourceUtils.getFile("classpath:pikapp-ventas-firebase-adminsdk-c4e0i-efcc603ecc.json");
				File file = ResourceUtils.getFile("classpath:merci-db.json");
				// final FileInputStream serviceAccount = new
				// FileInputStream("c:/merci-db.json");
				final FileInputStream serviceAccount = new FileInputStream(file);

				credentials = GoogleCredentials.fromStream(serviceAccount);
				System.out.println("Credentials OK");
			} catch (IOException e) {
				System.out.println("Error while reading Firebase config file." + e);
				throw new IllegalStateException(e);
			}
			final FirebaseOptions options = new FirebaseOptions.Builder()
					.setDatabaseUrl("https://pikap-ventas.firebaseio.com").setCredentials(credentials).build();
			FirebaseApp.initializeApp(options);
			final Firestore firestore = FirestoreClient.getFirestore();
			return firestore;
		} else {
			return FirebaseWebPayService.dbToken;
		}

	}

	public static Firestore inicializarFirestorePikapp() {
		if (FirebaseWebPayService.dbPikapp == null) {
			GoogleCredentials credentials;
			try {
				 File file = ResourceUtils.getFile("classpath:merci-db.json");
				//File file = ResourceUtils.getFile("classpath:pikapp-dev-269319-firebase-adminsdk-4zd99-15fc2d73c8.json");
				FileInputStream serviceAccount = new FileInputStream(file);

				credentials = GoogleCredentials.fromStream(serviceAccount);
				System.out.println("Credentials OK");
			} catch (IOException e) {
				System.out.println("Error while reading Firebase config file." + e);
				throw new IllegalStateException(e);
			}
			FirebaseOptions options = new FirebaseOptions.Builder()
					.setDatabaseUrl("https://pikapp-dev-269319.firebaseio.com").setCredentials(credentials).build();
//			FirebaseOptions options = new FirebaseOptions.Builder()
//					.setDatabaseUrl("https://merci-app-3c551.firebaseio.com").setCredentials(credentials).build();
			Firestore firestore = FirestoreClient.getFirestore(FirebaseApp.initializeApp(options, "merci-app-3c551"));
			return firestore;
		} else {
			return FirebaseWebPayService.dbPikapp;
		}

	}

	public void saveTokenBilling(String idBilling, String token) throws InterruptedException, ExecutionException {
		// Create a Map to store the data we want to set
		Map<String, Object> docData = new HashMap<>();
		docData.put("idBilling", idBilling);
		docData.put("token", token);
		// Add a new document (asynchronously) in collection "cities" with id "LA"
		ApiFuture<DocumentReference> future = dbToken.collection("billing").add(docData);
		// ...
		// future.get() blocks on response
		System.out.println("Update time : " + future.get().getId());

	}

	public Billing findBillingByToken(String token) throws Exception {
		Billing bill = new Billing();
		// [START fs_get_doc_as_map]
		CollectionReference billing = dbToken.collection("billing");
		Query query = billing.whereEqualTo("token", token);
		// retrieve query results asynchronously using query.get()
		ApiFuture<QuerySnapshot> querySnapshot = query.get();

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

	public void updateBilling(OneClickPayOutput respuestaTransbank, CompraOneClick compra, String base64) {
		Map<String, Object> billing = new HashMap<>();

		billing.put("status", 2);
		billing.put("qrPurchase", base64);
		billing.put("transactionId", respuestaTransbank.getTransactionId());
		billing.put("payFormat", respuestaTransbank.getCreditCardType().value());
		billing.put("last4CardDigits", respuestaTransbank.getLast4CardDigits());
		billing.put("transBankResponse", respuestaTransbank.toString());

		DocumentReference compraPikapp = dbPikapp.collection("compras").document(compra.getSessionId());
		//dbPikapp.collection("compras").document(compra.getSessionId()).update(billing);
		ApiFuture<WriteResult> result = compraPikapp.update(billing);
	}
}
