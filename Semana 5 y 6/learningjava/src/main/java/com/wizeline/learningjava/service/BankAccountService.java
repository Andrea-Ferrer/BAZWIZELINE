package com.wizeline.learningjava.service;

import java.util.List;
import java.util.Map;

import com.wizeline.learningjava.model.BankAccountDTO;

public interface BankAccountService {

	List<BankAccountDTO> getAccounts();

	Map<String, List<BankAccountDTO>> getAccountsGroupByType();

	BankAccountDTO getAccountDetails(String user, String lastUsage);

	void deleteAccounts();

	List<BankAccountDTO> getAccountByUser(String user);
}