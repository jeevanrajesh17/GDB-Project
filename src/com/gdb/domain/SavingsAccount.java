
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
}
