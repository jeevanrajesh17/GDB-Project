
package com.gdb.domain;

import com.gdb.exceptions.AccountException;
import com.gdb.exceptions.InsufficientBalanceException;

public class SalaryAccount extends AbstractAccount {

    private String employerName;
    private int inactiveMonths;

    public SalaryAccount(String accountNumber, String name, int age,
                         double balance, String status, String pin,
                         String employerName, int inactiveMonths) {

        super(accountNumber, name, age, balance,
              "SALARY", status, pin);

        this.employerName = employerName;
        this.inactiveMonths = inactiveMonths;
    }

    @Override
    protected void processDebit(double amount) throws AccountException {

        if (amount > getBalance()) {
            throw new InsufficientBalanceException(
                    "Insufficient funds in account");
        }

        balance -= amount;
    }

    public String getEmployerName() {
        return employerName;
    }

    public void setEmployerName(String employerName) {
        this.employerName = employerName;
    }

    public int getInactiveMonths() {
        return inactiveMonths;
    }

    public void setInactiveMonths(int inactiveMonths) {
        this.inactiveMonths = inactiveMonths;
    }
}
