

package com.wizeline.DTO;

import com.wizeline.enums.AccountType;

import java.time.LocalDateTime;

/**
 * DTO que representa la información de una cuenta de banco.
 * Created by jose.vazquez on 07/09/22
 */
public class BankAccountDTO {

    public void setLastUsage(String lastUsage) {
        this.lastUsage = lastUsage;
    }

    private long accountNumber;

    private String accountName;

    private String user;

    private double accountBalance;

    private AccountType accountType;

    private String country;

    private boolean accountActive;

    private LocalDateTime creationDate;

    private String lastUsage;

    public LocalDateTime getCreationDate() {
        return creationDate;

    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getLastUsage() {
        return lastUsage;
    }

    public void setLastUsage(LocalDateTime lastUsage) {
        this.lastUsage = lastUsage;
    }

    private LocalDateTime creationDate; // <- Cambiar a tipo de dato LocalDateTime
    private LocalDateTime lastUsage; // <- Crear nueva propiedad

    public long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(double accountBalance) {
        this.accountBalance = accountBalance;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public boolean isAccountActive() {
        return accountActive;
    }

    public void setAccountActive(boolean accountActive) {
        this.accountActive = accountActive;
    }


}
