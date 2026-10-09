
package com.gdb.domain;

import com.gdb.exceptions.AccountException;
import com.gdb.exceptions.InsufficientBalanceException;

public class CurrentAccount extends AbstractAccount {

    private double overdraftLimit;

    public CurrentAccount(String accountNumber, String name, int age,
                          double balance, String status, String pin,
                          double overdraftLimit) {

        super(accountNumber, name, age, balance,
              "CURRENT", status, pin);

        this.overdraftLimit = overdraftLimit;
    }

    @Override
    protected void processDebit(double amount) throws AccountException {

        if (amount > getBalance() + overdraftLimit) {
            throw new InsufficientBalanceException(
                    "Insufficient funds in account");
        }

        balance -= amount;
    }

    public double getOverdraftLimit() {
        return overdraftLimit;
    }

    public void setOverdraftLimit(double overdraftLimit) {
        this.overdraftLimit = overdraftLimit;
    }
}
