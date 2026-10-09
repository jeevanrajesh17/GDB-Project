
package com.gdb.domain;

public class SavingsAccount extends Account {

    private double minBalance;
    private double interestRate;

    public SavingsAccount(String accountNumber, String name, int age,
                          double balance, String status, String pin,
                          double minBalance, double interestRate) {
        super(accountNumber, name, age, balance, "SAVINGS", status, pin);
        this.minBalance = minBalance;
        this.interestRate = interestRate;
    }

    public void applyInterest() {
        double interest = getBalance() * interestRate / 100.0;
        setBalance(getBalance() + interest);
    }

    public double getMinBalance() {
        return minBalance;
    }

    public void setMinBalance(double minBalance) {
        this.minBalance = minBalance;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }



@Override
public boolean withdraw(double amount, String enteredPin)
        throws com.gdb.exceptions.InvalidPinException,
               com.gdb.exceptions.InactiveAccountException,
               com.gdb.exceptions.InvalidAmountException,
               com.gdb.exceptions.InsufficientBalanceException,
               com.gdb.exceptions.MinimumBalanceViolationException {

    // Validate PIN first
    if (!validatePin(enteredPin)) {
        throw new com.gdb.exceptions.InvalidPinException(
                "Invalid PIN entered");
    }

    // Check account status
    if (!"ACTIVE".equals(getStatus())) {
        throw new com.gdb.exceptions.InactiveAccountException(
                "Account is not active");
    }

    // Validate withdrawal amount
    if (amount <= 0) {
        throw new com.gdb.exceptions.InvalidAmountException(
                "Withdrawal amount must be positive");
    }

    // Check available balance
    if (amount > getBalance()) {
        throw new com.gdb.exceptions.InsufficientBalanceException(
                "Insufficient funds in account");
    }

    // Enforce savings minimum balance
    if (getBalance() - amount < minBalance) {
        throw new com.gdb.exceptions.MinimumBalanceViolationException(
                "Minimum balance requirement violated");
    }

    // All checks passed
    setBalance(getBalance() - amount);
    return true;
}




  }
