package com.gdb.domain;

import com.gdb.exceptions.InvalidAmountException;
import com.gdb.exceptions.InactiveAccountException;
import com.gdb.exceptions.InsufficientBalanceException;
import com.gdb.exceptions.InvalidPinException;
import com.gdb.exceptions.MinimumBalanceViolationException;

public class Account {

    private String accountNumber;
    private String name;
    private int age;
    private double balance;
    private String accountType;
    private String status;
    private String pin;

    private static final double MINIMUM_BALANCE = 0.0;

    public Account(String accountNumber, String name, int age,
                   double balance, String accountType, String status,
                   String pin) {

        if (age < 18) {
            throw new IllegalArgumentException(
                    "Customer must be at least 18 years old.");
        }

        if (balance < 0) {
            throw new IllegalArgumentException(
                    "Initial balance cannot be negative.");
        }

        if (!isValidPin(pin)) {
            throw new IllegalArgumentException(
                    "PIN must contain exactly 4 digits.");
        }

        this.accountNumber = accountNumber;
        this.name = name;
        this.age = age;
        this.balance = balance;
        this.accountType = accountType;
        this.status = status;
        this.pin = pin;
    }

    private boolean isValidPin(String pin) {
        return pin != null && pin.matches("\\d{4}");
    }

    public boolean validatePin(String inputPin) {
        return pin != null && pin.equals(inputPin);
    }

    public boolean changePin(String oldPin, String newPin) {
        if (!validatePin(oldPin) || !isValidPin(newPin)) {
            return false;
        }

        pin = newPin;
        System.out.println("PIN Changed Successfully.");
        return true;
    }

    public boolean deposit(double amount) throws InvalidAmountException {
    if (amount <= 0) {
        throw new InvalidAmountException(
                "Deposit amount must be positive");
    }

    balance += amount;
    return true;
}

    public boolean withdraw(double amount, String enteredPin)
            throws InvalidPinException,
                   InactiveAccountException,
                   InvalidAmountException,
                   InsufficientBalanceException,
                   MinimumBalanceViolationException {

        if (!validatePin(enteredPin)) {
            throw new InvalidPinException("Invalid PIN entered");
        }

        if (!"ACTIVE".equals(status)) {
            throw new InactiveAccountException(
                    "Account is not active");
        }

        if (amount <= 0) {
            throw new InvalidAmountException(
                    "Withdrawal amount must be positive");
        }

        if (amount > balance) {
            throw new InsufficientBalanceException(
                    "Insufficient funds in account");
        }

        double remainingBalance = balance - amount;

        if (remainingBalance < MINIMUM_BALANCE) {
            throw new MinimumBalanceViolationException(
                    "Minimum balance requirement violated");
        }

        balance = remainingBalance;
        return true;
    }

    public void suspend() {
        status = "SUSPENDED";
        System.out.println("Account Suspended.");
    }

    public void activate() {
        status = "ACTIVE";
        System.out.println("Account Re-Activated.");
    }

    public void close() {
        status = "CLOSED";
    }

    public void displayAccountInfo() {
        System.out.println("Account Number: " + accountNumber);
        System.out.println("Name: " + name);
        System.out.println("Age: " + age);
        System.out.println("Balance: Rs " + balance);
        System.out.println("Account Type: " + accountType);
        System.out.println("Status: " + status);
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        if (age < 18) {
            throw new IllegalArgumentException(
                    "Customer must be at least 18 years old.");
        }
        this.age = age;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        if (balance < 0) {
            throw new IllegalArgumentException(
                    "Balance cannot be negative.");
        }
        this.balance = balance;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getStatus() {
        return status;
    }
}
