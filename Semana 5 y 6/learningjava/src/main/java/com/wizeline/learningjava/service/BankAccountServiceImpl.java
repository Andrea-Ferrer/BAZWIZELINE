
package com.wizeline.learningjava.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.wizeline.learningjava.model.BankAccountDTO;
import com.wizeline.learningjava.repository.BankingAccountRepository;
import com.wizeline.learningjava.enums.Country;

import static com.wizeline.learningjava.utils.Utils.getCountry;
import static com.wizeline.learningjava.utils.Utils.pickRandomAccountType;
import static com.wizeline.learningjava.utils.Utils.randomAcountNumber;
import static com.wizeline.learningjava.utils.Utils.randomBalance;
import static com.wizeline.learningjava.utils.Utils.randomInt;

@Service
public class BankAccountServiceImpl implements BankAccountService {

	private static final Logger LOGGER = Logger.getLogger(BankAccountServiceImpl.class.getName());

	@Autowired
	private BankingAccountRepository bankAccountRepository;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public List<BankAccountDTO> getAccounts() {
		List<BankAccountDTO> accountDTOList = new ArrayList<>();
		BankAccountDTO bankAccountOne = buildBankAccount("user3@wizeline.com", true, Country.MX,
				LocalDateTime.now().minusDays(7));
		accountDTOList.add(bankAccountOne);
		mongoTemplate.save(bankAccountOne);
		BankAccountDTO bankAccountTwo = buildBankAccount("user1@wizeline.com", false, Country.FR,
				LocalDateTime.now().minusMonths(2));
		accountDTOList.add(bankAccountTwo);
		mongoTemplate.save(bankAccountTwo);
		BankAccountDTO bankAccountThree = buildBankAccount("user2@wizeline.com", false, Country.US,
				LocalDateTime.now().minusYears(4));
		accountDTOList.add(bankAccountThree);
		mongoTemplate.save(bankAccountThree);
		mongoTemplate.findAll(BankAccountDTO.class).stream().map(bankAccountDTO -> bankAccountDTO.getUserName())
				.forEach(
						(user) -> {
							LOGGER.info("User stored in bankAccountCollection " + user);
						});
		return accountDTOList;
	}

	@Override
	public List<BankAccountDTO> getAccountByUser(String user) {
		//Buscamos todos aquellos registros de tipo BankAccountDTO
		//que cumplen con la criteria de que el userName haga match
		//con la variable user
		Query query = new Query();
		query.addCriteria(Criteria.where("userName").is(user));
		return mongoTemplate.find(query, BankAccountDTO.class);
	}

	@Override
	public BankAccountDTO getAccountDetails(String user, String lastUsage) {
		DateTimeFormatter dateformatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		LocalDate usage = LocalDate.parse(lastUsage, dateformatter);
		return buildBankAccount(user, true, Country.MX, usage.atStartOfDay());
	}

	@Override

	public Map<String, List<BankAccountDTO>> getAccountsGroupByType() {
		List<BankAccountDTO> accounts = getAccounts();
		Function<BankAccountDTO, String> groupFunction = (account) -> account.getAccountType().toString();
		Map<String, List<BankAccountDTO>> groupedAccounts = accounts.stream()
				.collect(Collectors.groupingBy(groupFunction));
		return groupedAccounts;
	};

	// Creación de tipo de dato BankAccount
	private BankAccountDTO buildBankAccount(String user, boolean isActive, Country country, LocalDateTime lastUsage) {
		BankAccountDTO bankAccountDTO = new BankAccountDTO();
		bankAccountDTO.setAccountNumber(randomAcountNumber());
		bankAccountDTO.setAccountName("Dummy Account ".concat(randomInt()));
		bankAccountDTO.setUserName(user);
		bankAccountDTO.setAccountBalance(randomBalance());
		bankAccountDTO.setAccountType(pickRandomAccountType());
		bankAccountDTO.setCountry(getCountry(country));
		bankAccountDTO.getLastUsage();
		bankAccountDTO.setCreationDate(lastUsage);
		bankAccountDTO.setAccountActive(isActive);
		return bankAccountDTO;
	}

	@Override
	public void deleteAccounts() {
		bankAccountRepository.deleteAll();
	}
}