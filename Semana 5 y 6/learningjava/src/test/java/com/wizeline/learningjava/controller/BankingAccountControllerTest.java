package com.wizeline.learningjava.controller;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.jboss.logging.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.wizeline.learningjava.client.AccountsJSONClient;
import com.wizeline.learningjava.enums.Country;
import com.wizeline.learningjava.model.BankAccountDTO;
import com.wizeline.learningjava.model.Post;
import com.wizeline.learningjava.model.ResponseDTO;
import com.wizeline.learningjava.service.BankAccountServiceImpl;
import com.wizeline.learningjava.service.UserServiceImpl;
import com.wizeline.learningjava.utils.Utils;

@ExtendWith(MockitoExtension.class)
public class BankingAccountControllerTest {

	private static final Logger LOGGER = Logger.getLogger(BankingAccountControllerTest.class.getName());

	@Mock
	private BankAccountServiceImpl bankAccountServiceImpl;

	@Mock
	private UserServiceImpl userServiceImpl;

	@InjectMocks
	private BankingAccountController bankingAccountController;

	@InjectMocks
	Utils utils;

	@Mock
	AccountsJSONClient accountsJSONClient;

	//Pueba Unitaria Happy Path /UsegetrAccount
	@Test
	void DadoUnUserPasswordLastUsageCorrectos_BuscaCuentaDeUserDespuesDeLogueo_RegresaCuentadelUsuario() {
		LOGGER.info(
				"Se prueba Happy Path de endpoint /getUserAccount con user y contraseña user1@wizeline.com, conTr@53na");
		String user = "user1@wizeline.com";
		String password = "conTr@53na";
		ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setCode("OK000");
		responseDTO.setStatus("Usuario encontrado");
		String lastUsage = "07-10-2022";
		BankAccountDTO bankAccountDTO = new BankAccountDTO();
		bankAccountDTO.setAccountNumber(utils.randomAcountNumber());
		bankAccountDTO.setAccountName("Dummy Account ".concat(utils.randomInt()));
		bankAccountDTO.setUserName(user);
		bankAccountDTO.setAccountBalance(utils.randomBalance());
		bankAccountDTO.setAccountType(utils.pickRandomAccountType());
		bankAccountDTO.setCountry(utils.getCountry(Country.MX));
		bankAccountDTO.getLastUsage();
		DateTimeFormatter dateformatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		LocalDate usage = LocalDate.parse(lastUsage, dateformatter);
		bankAccountDTO.setCreationDate(usage.atStartOfDay());
		bankAccountDTO.setAccountActive(true);
		when(userServiceImpl.login(user, password)).thenReturn(responseDTO);
		when(bankAccountServiceImpl.getAccountDetails(user, lastUsage)).thenReturn(bankAccountDTO);
		final ResponseEntity<?> respuesta = bankingAccountController.getUserAccount(user, password, lastUsage);
		assertAll(
				() -> assertNotNull(respuesta),
				() -> assertEquals(HttpStatus.OK, respuesta.getStatusCode()),
				() -> assertEquals(bankAccountDTO, respuesta.getBody()));
		LOGGER.info("Resultado de la prueba /UsegetrAccount: " + respuesta.getStatusCode());
	}

	//Pueba Unitaria Edge Case /getUserAccount
	@Test
	void DadoUnUserPasswordLastUsageIncorrectos_BuscaCuentaDeUserDespuesDeLogueo_RegresaCuentadelUsuario() {
		LOGGER.info(
				"Se prueba Edge Case de endpoint /getUserAccount con user y contraseña user1@wizeline.com, conTr@53na");
		String user = "user69@wizeline.com";
		String password = "conTr@53na";
		ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setCode("ER004");
		responseDTO.setStatus("fail");
		String lastUsage = "07-03-2022";
		when(userServiceImpl.login(user, password)).thenReturn(responseDTO);
		final ResponseEntity<?> respuesta = bankingAccountController.getUserAccount(user, password, lastUsage);
		assertAll(
				() -> assertNotNull(respuesta),
				() -> assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode()));
		LOGGER.info("Resultado de la prueba /getUserAccount: " + respuesta.getStatusCode());
	}

	//Prueba Unitaria Happy Path de endpoint /getAccountByUser
	@Test
	void dadoUsuarioConCuenta_BuscaEnBDCuentasDelUsuario_RegresaListaDeCuentas() {
		LOGGER.info(
				"Se prueba Happy Path de endpoint /getAccountByUser con user y contraseña user1@wizeline.com, conTr@53na");
		String user = "user1@wizeline.com";
		BankAccountDTO bankAccountDTO = new BankAccountDTO();
		bankAccountDTO.setAccountNumber(utils.randomAcountNumber());
		bankAccountDTO.setAccountName("Dummy Account ".concat(utils.randomInt()));
		bankAccountDTO.setUserName(user);
		bankAccountDTO.setAccountBalance(utils.randomBalance());
		bankAccountDTO.setAccountType(utils.pickRandomAccountType());
		bankAccountDTO.setCountry(utils.getCountry(Country.MX));
		bankAccountDTO.getLastUsage();
		DateTimeFormatter dateformatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		LocalDate usage = LocalDate.parse("09-10-2022", dateformatter);
		bankAccountDTO.setCreationDate(usage.atStartOfDay());
		bankAccountDTO.setAccountActive(true);
		List<BankAccountDTO> listaDeCuentas = new ArrayList<>();
		listaDeCuentas.add(bankAccountDTO);
		when(bankAccountServiceImpl.getAccountByUser(user)).thenReturn(listaDeCuentas);
		final ResponseEntity<?> respuesta = bankingAccountController.getAccountByUser(user);
		assertAll(
				() -> assertNotNull(respuesta),
				() -> assertEquals(HttpStatus.OK, respuesta.getStatusCode()),
				() -> assertEquals(listaDeCuentas, respuesta.getBody()));
		LOGGER.info("Resultado de la prueba /getAccountByUser: " + respuesta.getStatusCode());
	}

	//Prueba Unitaria Edge Case  de endpoint /getAccountByUser
	@Test
	void dadoUsuarioConCuenta_BuscaEnBDCuentasDelUsuario_RegresaNotFound() {
		LOGGER.info(
				"Se prueba Edge Case de endpoint /getAccountByUser con user y contraseña user1@wizeline.com, conTr@53na");
		String user = "user69@wizeline.com";
		when(bankAccountServiceImpl.getAccountByUser(user)).thenReturn(null);
		final ResponseEntity<?> respuesta = bankingAccountController.getAccountByUser(user);
		assertAll(
				() -> assertNotNull(respuesta),
				() -> assertEquals(HttpStatus.NOT_FOUND, respuesta.getStatusCode()));
		LOGGER.info("Resultado de la prueba /getAccountByUser: " + respuesta.getStatusCode());
	}

	//Prueba Unitaria Happy Path de endpoint /getExternalUser/{userId}
	@Test
	void dadoUnIdCorrecto_ConsultaApiExternaConFeing_RegresaObjetoPostTest() {
		LOGGER.info(
				"Se prueba Happy Path de endpoint  /getExternalUser/{userId} con id dentro del rango");
		Long id = (long) 1;
		Post postTest = new Post();
		postTest.setUserId("External user " + String.valueOf(id));
		postTest.setBody("No info in accountBalance since it is an external user");
		postTest.setTitle("No info in title since it is an external user");
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("Content-Type", "application/json; charset=UTF-8");
		when(accountsJSONClient.getPostById(id)).thenReturn(postTest);
		final ResponseEntity<?> respuesta = bankingAccountController.getExternalUser(id);
		assertAll(
				() -> assertNotNull(respuesta),
				() -> assertEquals(HttpStatus.OK, respuesta.getStatusCode()),
				() -> assertEquals(postTest, respuesta.getBody()));
		LOGGER.info("Resultado de la prueba /getExternalUser/{userId}: " + respuesta.getStatusCode());

	}

	//Prueba Unitaria Edge Case de endpoint /getExternalUser/{userId}
	@Test
	void dadoUnIdCorrecto_ConsultaApiExternaConFeing_RegresaErrorBadRequest() {
		LOGGER.info(
				"Se prueba Edge Case de endpoint  /getExternalUser/{userId} con id dentro del rango");
		Long id = (long) 101;
		when(accountsJSONClient.getPostById(id)).thenReturn(null);
		final ResponseEntity<?> respuesta = bankingAccountController.getExternalUser(id);
		assertAll(
				() -> assertNotNull(respuesta),
				() -> assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode()));
		LOGGER.info("Resultado de la prueba /getExternalUser/{userId}: " + respuesta.getStatusCode());
	}

	/*Los siguientes test solo se pueden probar con happy path
	 * ya que solo son peticiones GET sin parámetros
	 * sería dificil probar un caso de error o hacerlo fallar
	 */

	@Test
	void testGetAccounts() {
		LOGGER.info(
				"Se prueba Happy Path de endpoint  /getAccounts con id dentro del rango");
		List<BankAccountDTO> accountDTOList = new ArrayList<>();
		BankAccountDTO bankAccountOne = buildBankAccount("user3@wizeline.com", true, Country.MX,
				LocalDateTime.now().minusDays(7));
		accountDTOList.add(bankAccountOne);
		BankAccountDTO bankAccountTwo = buildBankAccount("user1@wizeline.com", false, Country.FR,
				LocalDateTime.now().minusMonths(2));
		accountDTOList.add(bankAccountTwo);
		BankAccountDTO bankAccountThree = buildBankAccount("user2@wizeline.com", false, Country.US,
				LocalDateTime.now().minusYears(4));
		accountDTOList.add(bankAccountThree);
		when(bankAccountServiceImpl.getAccounts()).thenReturn(accountDTOList);
		final ResponseEntity<?> respuesta = bankingAccountController.getAccounts();
		assertAll(
				() -> assertNotNull(respuesta),
				() -> assertEquals(HttpStatus.OK, respuesta.getStatusCode()),
				() -> assertEquals(accountDTOList, respuesta.getBody()));
		LOGGER.info("Resultado de la prueba /getAccounts: " + respuesta.getStatusCode());
	}

	@Test
	void testGetAccountsGroupByType() {
		LOGGER.info(
				"Se prueba Happy Path de endpoint  /getAccountsGroupByType");
		List<BankAccountDTO> accountDTOList = new ArrayList<>();
		BankAccountDTO bankAccountOne = buildBankAccount("user3@wizeline.com", true, Country.MX,
				LocalDateTime.now().minusDays(7));
		accountDTOList.add(bankAccountOne);
		BankAccountDTO bankAccountTwo = buildBankAccount("user1@wizeline.com", false, Country.FR,
				LocalDateTime.now().minusMonths(2));
		accountDTOList.add(bankAccountTwo);
		BankAccountDTO bankAccountThree = buildBankAccount("user2@wizeline.com", false, Country.US,
				LocalDateTime.now().minusYears(4));
		accountDTOList.add(bankAccountThree);
		Function<BankAccountDTO, String> groupFunction = (account) -> account.getAccountType().toString();
		Map<String, List<BankAccountDTO>> groupedAccounts = accountDTOList.stream()
				.collect(Collectors.groupingBy(groupFunction));
		when(bankAccountServiceImpl.getAccountsGroupByType()).thenReturn(groupedAccounts);
		final ResponseEntity<?> respuesta = bankingAccountController.getAccountsGroupByType();
		assertAll(
				() -> assertNotNull(respuesta),
				() -> assertEquals(HttpStatus.OK, respuesta.getStatusCode()),
				() -> assertEquals(groupedAccounts, respuesta.getBody()));
		LOGGER.info("Resultado de la prueba /getAccountsGroupByType: " + respuesta.getStatusCode());

	}

	@Test
	void testDeleteAccounts() {
		LOGGER.info(
				"Se prueba Happy Path de endpoint  /deleteAccounts");
		final ResponseEntity<?> respuesta = bankingAccountController.deleteAccounts();
		assertEquals(HttpStatus.OK, respuesta.getStatusCode());
		LOGGER.info("Resultado de la prueba /deleteAccounts: " + respuesta.getStatusCode());

	}

	private BankAccountDTO buildBankAccount(String user, boolean isActive, Country country, LocalDateTime lastUsage) {
		BankAccountDTO bankAccountDTO = new BankAccountDTO();
		bankAccountDTO.setAccountNumber(Utils.randomAcountNumber());
		bankAccountDTO.setAccountName("Dummy Account ".concat(Utils.randomInt()));
		bankAccountDTO.setUserName(user);
		bankAccountDTO.setAccountBalance(Utils.randomBalance());
		bankAccountDTO.setAccountType(Utils.pickRandomAccountType());
		bankAccountDTO.setCountry(Utils.getCountry(country));
		bankAccountDTO.getLastUsage();
		bankAccountDTO.setCreationDate(lastUsage);
		bankAccountDTO.setAccountActive(isActive);
		return bankAccountDTO;
	}
}
