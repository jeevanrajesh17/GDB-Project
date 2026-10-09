
package com.gdb.domain;

import com.gdb.exceptions.AccountException;
import com.gdb.exceptions.InsufficientBalanceException;
import com.gdb.exceptions.MinimumBalanceViolationException;

public class SavingsAccount extends AbstractAccount {

    private double minBalance;
    private double interestRate;

    public SavingsAccount(String accountNumber, String name, int age,
                          double balance, String status, String pin,
                          double minBalance, double interestRate) {

        super(accountNumber, name, age, balance,
              "SAVINGS", status, pin);

        this.minBalance = minBalance;
        this.interestRate = interestRate;
    }

    @Override
    protected void processDebit(double amount) throws AccountException {

        if (amount > getBalance()) {
            throw new InsufficientBalanceException(
                    "Insufficient funds in account");
        }

        if (getBalance() - amount < minBalance) {
            throw new MinimumBalanceViolationException(
                    "Minimum balance requirement violated");
        }

        balance -= amount;
    }

    public void applyInterest() {
        double interest = getBalance() * interestRate / 100.0;
        balance += interest;
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
