package com.wizeline.learningjava.controller;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wizeline.learningjava.model.ResponseDTO;
import com.wizeline.learningjava.model.UserDTO;
import com.wizeline.learningjava.service.UserService;

@RequestMapping("/api")
@RestController
public class UserController {

	@Autowired
	private UserService userService;

	private static final Logger LOGGER = Logger.getLogger(UserController.class.getName());

	@GetMapping(value = "/login", produces = "application/json")
	public ResponseEntity<?> loginUser(@RequestParam String user, @RequestParam String password) {
		LOGGER.info("LearningJava - Procesando peticion HTTP de tipo GET");
		ResponseDTO response = new ResponseDTO();
		response = userService.login(user, password);
		if (response.getCode().equals("OK000")) {
			LOGGER.info("Login - Completed");
			return new ResponseEntity<>(response, HttpStatus.OK);
		} else {
			LOGGER.info("Login - Error");
			return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
		}
	}

	@PostMapping(value = "/createUser", produces = "application/json")
	public ResponseEntity<?> createUserAccount(@RequestBody UserDTO usuario) {
		LOGGER.info("LearningJava - Procesando peticion HTTP de tipo POST - Starting... ");
		ResponseDTO response = new ResponseDTO();
		response = userService.createUser(usuario.getUser(), usuario.getPassword());
		if (response.getCode().equals("OK000")) {
			LOGGER.info("Create user - Completed");
			return new ResponseEntity<>(response, HttpStatus.OK);
		} else {
			LOGGER.info("Create user - ERROR");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping(value = "/updatePassword", produces = "application/json")
	public ResponseEntity<?> updateUserPassword(@RequestBody UserDTO usuario) {
		LOGGER.info("LearningJava - Procesando peticion HTTP de tipo POST - Starting... ");
		ResponseDTO response = new ResponseDTO();
		response = userService.updatePassword(usuario.getUser(), usuario.getPassword());
		if (response.getCode().equals("OK000")) {
			LOGGER.info("Update password - Completed");
			return new ResponseEntity<>(response, HttpStatus.OK);
		} else {
			LOGGER.info("Update password - ERROR");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}

}