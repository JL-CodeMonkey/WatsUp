package com.watsup.server;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.Label;
import com.google.api.services.gmail.model.ListLabelsResponse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

@SpringBootApplication
@RestController
public class WatsUpServer {

	private static final String FROM_EMAIL = "watsup2425@gmail.com";

	public static void main(String[] args) {
		SpringApplication.run(WatsUpServer.class, args);
	}

	@GetMapping("/get_login_code")
	public ResponseEntity<String> getLoginCode(@RequestParam(value = "email") String toEmailAddress)
			throws GeneralSecurityException, MessagingException, IOException {
		// Test with this URL:  http://localhost:3000/get_login_code?email=j47ho%40uwaterloo.ca
		if (!toEmailAddress.toLowerCase().endsWith("@uwaterloo.ca")) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
		}
		GMailUtils.sendEmail(FROM_EMAIL, toEmailAddress);
		return ResponseEntity.ok(String.format("Login code has been sent to %s!", toEmailAddress));
	}

	@GetMapping("/validate_login_code")
	public ResponseEntity<String>  validateLoginCode(
			@RequestParam(value = "email") String toEmailAddress,
			@RequestParam(value = "code") String code
	) throws IOException, GeneralSecurityException {
		// Test with this URL:  http://localhost:3000/validate_login_code?email=j47ho%40uwaterloo.ca&code=976947
		if (!toEmailAddress.toLowerCase().endsWith("@uwaterloo.ca")) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
		}
		if (GMailUtils.validateCode(toEmailAddress, code)) {
			return ResponseEntity.ok(String.format("Validate code %s for email %s!", code, toEmailAddress));
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(String
					.format("Invalid code %s for email %s!", code, toEmailAddress));
		}
	}
}
