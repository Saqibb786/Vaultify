package com.example.bankingproject;

import com.google.firebase.database.IgnoreExtraProperties;
import java.util.ArrayList;
import java.util.List;

@IgnoreExtraProperties
public class User {
    private String name;
    private String bankPassword;
    private String accountNumber;
    private Long balance;
    private List<String> transactionHistory;

    public User() {
        this.transactionHistory = new ArrayList<>();
    }

    public User(String name, String bankPassword, String accountNumber) {
        this.name = name;
        this.bankPassword = bankPassword;
        this.accountNumber = accountNumber;
        this.balance = 1000L;
        this.transactionHistory = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBankPassword() {
        return bankPassword;
    }

    public void setBankPassword(String bankPassword) {
        this.bankPassword = bankPassword;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public List<String> getTransactionHistory() {
        return transactionHistory;
    }

    public void addTransaction(String transaction) {
        transactionHistory.add(transaction);
    }

    public static User getUser(String name, String bankPassword, String accountNumber) {
        return new User(name, bankPassword, accountNumber);
    }
}
