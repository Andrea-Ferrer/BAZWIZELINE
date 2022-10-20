package com.wizeline.learningjava.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.wizeline.learningjava.model.ResponseDTO;
import com.wizeline.learningjava.model.UserDTO;
import com.wizeline.learningjava.service.UserServiceImpl;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.logging.Logger;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

	private static final Logger LOGGER = Logger.getLogger(UserControllerTest.class.getName());

	@Mock
	private UserServiceImpl userServiceImpl;

	@InjectMocks
	private UserController userController;

	//Prueba Unitaria Happy Path /loginUser
	@Test
	public void DadoUnUusuarioYContraseniaCorrectos_CuandoBuscaUsuario_EntoncesRegresaStatusOk() {
		LOGGER.info(
				"Se prueba Happy Path de endpoint loginUser con parametros user: userwizeline1@gmail.com y password: conTr@53na");
		String user = "userwizeline1@gmail.com";
		String password = "conTr@53na";
		ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setCode("OK000");
		responseDTO.setStatus("Usuario encontrado");
		when(userServiceImpl.login(user, password)).thenReturn(responseDTO);
		final ResponseEntity<?> respuesta = userController.loginUser(user, password);
		assertAll(
				() -> assertNotNull(respuesta),
				() -> assertEquals(HttpStatus.OK, respuesta.getStatusCode()),
				() -> assertEquals(responseDTO, respuesta.getBody()));
		LOGGER.info("Resultado de la prueba: " + respuesta.getStatusCode());

	}

	//Prueba Unitaria EdgeCase /loginUser
	@Test
	public void DadoUnUusuarioYContraseniaIncorrectos_CuandoBuscaUsuario_EntoncesRegresaStatusUnauthorized() {
		LOGGER.info(
				"Se prueba Edge Case de endpoint loginUser con parametros user - userwizeline59@gmail.com - password: conTr@53na");
		String user = "userwizeline59@gmail.com";
		String password = "conTr@53na";
		ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setCode("ER004");
		responseDTO.setStatus("fail");
		when(userServiceImpl.login(user, password)).thenReturn(responseDTO);
		final ResponseEntity<?> respuesta = userController.loginUser(user, password);
		assertAll(
				() -> assertNotNull(respuesta),
				() -> assertEquals(HttpStatus.UNAUTHORIZED, respuesta.getStatusCode()),
				() -> assertEquals(responseDTO, respuesta.getBody()));
		LOGGER.info("Resultado de la prueba: " + respuesta.getStatusCode());
	}

	//Prueba Unitaria Happy Path /createUser
	@Test
	void DadoUsuarioYContraseniaDeUsuarioNoRegistrado_CuandoGUardaUsuario_EntoncesRegresaStatusOk() {
		LOGGER.info(
				"Se prueba Edge Case de endpoint createUser con parametros user - userwizeline59@gmail.com - password: conTr@53na");

		UserDTO userDTO = new UserDTO();
		userDTO.setUser("userwizeline2@gmail.com");
		userDTO.setPassword("conTr@53na");
		ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setCode("OK000");
		responseDTO.setStatus("success");
		when(userServiceImpl.createUser(userDTO.getUser(), userDTO.getPassword())).thenReturn(responseDTO);
		final ResponseEntity<?> respuesta = userController.createUserAccount(userDTO);
		assertAll(
				() -> assertNotNull(respuesta),
				() -> assertEquals(HttpStatus.OK, respuesta.getStatusCode()),
				() -> assertEquals(responseDTO, respuesta.getBody()));
		LOGGER.info("Resultado de la prueba: " + respuesta.getStatusCode());
	}

	//Prueba Unitaria Edge Case /createUser
	@Test
	void DadoUsuarioYContraseniaDeUsuarioRegistrado_CuandoGuardaUsuario_EntoncesRegresaStatusBADREQUEST() {
		LOGGER.info(
				"Se prueba Edge Case de endpoint createUser con parametros user - userwizeline59@gmail.com - password: conTr@53na");
		UserDTO userDTO = new UserDTO();
		userDTO.setUser("userwizeline2@gmail.com");
		userDTO.setPassword("conTr@53na");
		ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setCode("ER004");
		responseDTO.setStatus("fail");
		when(userServiceImpl.createUser(userDTO.getUser(), userDTO.getPassword())).thenReturn(responseDTO);
		final ResponseEntity<?> respuesta = userController.createUserAccount(userDTO);
		assertAll(
				() -> assertNotNull(respuesta),
				() -> assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode()),
				() -> assertEquals(responseDTO, respuesta.getBody()));
		LOGGER.info("Resultado de la prueba: " + respuesta.getStatusCode());
	}

	//Prueba Unitaria Happy Path /updateUserPassword
	@Test
	void DadoUsuarioYContraseniaDeUsuarioExistente_CuandoActualizaUsuario_EntoncesRegresaStatusOk() {
		LOGGER.info(
				"Se prueba Edge Case de endpoint createUser con parametros user - userwizeline59@gmail.com - password: conTr@53na");
		UserDTO userDTO = new UserDTO();
		userDTO.setUser("userwizeline2@gmail.com");
		userDTO.setPassword("conTr@53na");
		ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setCode("OK000");
		responseDTO.setStatus("success");
		when(userServiceImpl.updatePassword(userDTO.getUser(), userDTO.getPassword())).thenReturn(responseDTO);
		final ResponseEntity<?> respuesta = userController.updateUserPassword(userDTO);
		assertAll(
				() -> assertNotNull(respuesta),
				() -> assertEquals(HttpStatus.OK, respuesta.getStatusCode()),
				() -> assertEquals(responseDTO, respuesta.getBody()));
		LOGGER.info("Resultado de la prueba: " + respuesta.getStatusCode());
	}

	//Prueba Unitaria Edge Case /updateUserPassword
	@Test
	void DadoUsuarioYContraseniaDeUsuarioExistente_CuandoActualizaUsuario_EntoncesRegresaStatusBADREQUEST() {
		LOGGER.info(
				"Se prueba Edge Case de endpoint createUser con parametros user - userwizeline59@gmail.com - password: conTr@53na");
		UserDTO userDTO = new UserDTO();
		userDTO.setUser("userwizeline2@gmail.com");
		userDTO.setPassword("conTr@53na");
		ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setCode("ER004");
		responseDTO.setStatus("fail");
		when(userServiceImpl.updatePassword(userDTO.getUser(), userDTO.getPassword())).thenReturn(responseDTO);
		final ResponseEntity<?> respuesta = userController.updateUserPassword(userDTO);
		assertAll(
				() -> assertNotNull(respuesta),
				() -> assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode()),
				() -> assertEquals(responseDTO, respuesta.getBody()));
		LOGGER.info("Resultado de la prueba: " + respuesta.getStatusCode());
	}

}
