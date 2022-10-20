package com.wizeline.learningjava.controller;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wizeline.learningjava.client.AccountsJSONClient;
import com.wizeline.learningjava.model.BankAccountDTO;
import com.wizeline.learningjava.model.Post;
import com.wizeline.learningjava.model.ResponseDTO;
import com.wizeline.learningjava.service.BankAccountService;
import com.wizeline.learningjava.service.UserService;
import com.wizeline.learningjava.utils.Utils;

@RequestMapping("/api")
@RestController
public class BankingAccountController {

	private static final String SUCCESS_CODE = "OK000";

	@Autowired
	private BankAccountService bankAccountService;

	@Autowired
	private UserService userService;

	@Autowired
	private Utils utils;

	@Autowired
	AccountsJSONClient accountsJSONClient;

	@Value("${server.port}")
	private String port;

	private static final Logger LOGGER = Logger.getLogger(BankingAccountController.class.getName());
	String msgProcPeticion = "LearningJava - Inicia procesamiento de peticion ...";

	@GetMapping("/getUserAccount")
	public ResponseEntity<?> getUserAccount(@RequestParam String user, @RequestParam String password,
			@RequestParam String date) {
		LOGGER.info(msgProcPeticion);
		Instant inicioDeEjecucion = Instant.now();
		ResponseDTO response = new ResponseDTO();
		HttpHeaders responseHeaders = new HttpHeaders();
		String responseText = "";
		responseHeaders.set("Content-Type", "application/json; charset=UTF-8");
		if (utils.isDateFormatValid(date)) {
			// Valida el password del usuario (password)
			if (utils.isPasswordValid(password)) {
				response = userService.login(user, password);
				if (response.getCode().equals(SUCCESS_CODE)) {
					BankAccountDTO bankAccountDTO = bankAccountService.getAccountDetails(user, date);
					Instant finalDeEjecucion = Instant.now();
					LOGGER.info("LearningJava - Cerrando recursos ...");
					String total = new String(
							String.valueOf(Duration.between(inicioDeEjecucion, finalDeEjecucion).toMillis())
									.concat(" segundos."));
					LOGGER.info("Tiempo de respuesta: ".concat(total));
					return new ResponseEntity<>(bankAccountDTO, responseHeaders, HttpStatus.OK);
				} else {
					Instant finalDeEjecucion = Instant.now();
					LOGGER.info("LearningJava - Cerrando recursos ...");
					String total = new String(
							String.valueOf(Duration.between(inicioDeEjecucion, finalDeEjecucion).toMillis())
									.concat(" segundos."));
					LOGGER.info("Tiempo de respuesta: ".concat(total));
					responseText = "Error al iniciar sesión: user y/o password incorrecto";
					return new ResponseEntity<>(responseText, responseHeaders, HttpStatus.BAD_REQUEST);
				}
			} else {
				Instant finalDeEjecucion = Instant.now();
				LOGGER.info("LearningJava - Cerrando recursos ...");
				String total = new String(
						String.valueOf(Duration.between(inicioDeEjecucion, finalDeEjecucion).toMillis())
								.concat(" segundos."));
				LOGGER.info("Tiempo de respuesta: ".concat(total));
				responseText = "El password no tiene el formato correcto";
				return new ResponseEntity<>(responseText, responseHeaders, HttpStatus.BAD_REQUEST);
			}
		} else {
			Instant finalDeEjecucion = Instant.now();
			LOGGER.info("LearningJava - Cerrando recursos ...");
			String total = new String(
					String.valueOf(Duration.between(inicioDeEjecucion, finalDeEjecucion).toMillis())
							.concat(" segundos."));
			LOGGER.info("Tiempo de respuesta: ".concat(total));
			responseText = "La fecha no tiene el formato correcto";
			return new ResponseEntity<>(responseText, responseHeaders, HttpStatus.BAD_REQUEST);

		}
	}

	@GetMapping("/getAccountByUser")
	public ResponseEntity<?> getAccountByUser(@RequestParam String user) {
		LOGGER.info(msgProcPeticion);
		Instant inicioDeEjecucion = Instant.now();
		LOGGER.info("LearningJava - Procesando peticion HTTP de tipo GET");
		List<BankAccountDTO> accounts = bankAccountService.getAccountByUser(user);
		Instant finalDeEjecucion = Instant.now();
		LOGGER.info("LearningJava - Cerrando recursos ...");
		String total = new String(
				String.valueOf(Duration.between(inicioDeEjecucion, finalDeEjecucion).toMillis()).concat(" segundos."));
		LOGGER.info("Tiempo de respuesta: ".concat(total));
		if ((accounts) != null && !accounts.isEmpty()) {
			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.set("Content-Type", "application/json; charset=UTF-8");
			return new ResponseEntity<>(accounts, responseHeaders, HttpStatus.OK);
		} else {
			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.set("Content-Type", "application/json; charset=UTF-8");
			return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/getExternalUser/{userId}")
	public ResponseEntity<?> getExternalUser(@PathVariable Long userId) {
		try {
			Post postTest = accountsJSONClient.getPostById(userId);
			if (postTest != null) {
				postTest.setUserId("External user " + String.valueOf(userId));
				postTest.setBody("No info in accountBalance since it is an external user");
				postTest.setTitle("No info in title since it is an external user");
				HttpHeaders responseHeaders = new HttpHeaders();
				responseHeaders.set("Content-Type", "application/json; charset=UTF-8");
				return new ResponseEntity<>(postTest, responseHeaders, HttpStatus.OK);
			}
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
		}
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("Content-Type", "application/json; charset=UTF-8");
		return new ResponseEntity<>("id fuera de rango (1-100)", HttpStatus.BAD_REQUEST);

	}

	@GetMapping("/getAccounts")
	public ResponseEntity<List<BankAccountDTO>> getAccounts() {
		LOGGER.info("The port used is " + port);
		LOGGER.info(msgProcPeticion);
		Instant inicioDeEjecucion = Instant.now();
		LOGGER.info("LearningJava - Procesando peti2cion HTTP de tipo GET");
		List<BankAccountDTO> accounts = bankAccountService.getAccounts();
		Instant finalDeEjecucion = Instant.now();
		LOGGER.info("LearningJava - Cerrando recursos ...");
		String total = new String(
				String.valueOf(Duration.between(inicioDeEjecucion, finalDeEjecucion).toMillis()).concat(" segundos."));
		LOGGER.info("Tiempo de respuesta: ".concat(total));

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("Content-Type", "application/json; charset=UTF-8");
		return new ResponseEntity<>(accounts, responseHeaders, HttpStatus.OK);

	}

	@GetMapping("/getAccountsGroupByType")
	public ResponseEntity<Map<String, List<BankAccountDTO>>> getAccountsGroupByType() {
		LOGGER.info(msgProcPeticion);
		Instant inicioDeEjecucion = Instant.now();
		LOGGER.info("LearningJava - Procesando peticion HTTP de tipo GET");
		// Aqui implementaremos la programación funcional
		Map<String, List<BankAccountDTO>> groupedAccounts = bankAccountService.getAccountsGroupByType();
		Instant finalDeEjecucion = Instant.now();
		LOGGER.info("LearningJava - Cerrando recursos ...");
		String total = new String(
				String.valueOf(Duration.between(inicioDeEjecucion, finalDeEjecucion).toMillis()).concat(" segundos."));
		LOGGER.info("Tiempo de respuesta: ".concat(total));
		return new ResponseEntity<>(groupedAccounts, HttpStatus.OK);
	}

	@DeleteMapping("/deleteAccounts")
	public ResponseEntity<String> deleteAccounts() {
		bankAccountService.deleteAccounts();
		return new ResponseEntity<>("All accounts deleted", HttpStatus.OK);
	}

}
