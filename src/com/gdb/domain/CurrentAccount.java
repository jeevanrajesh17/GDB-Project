
package com.gdb.domain;

public class CurrentAccount extends Account {

    private double overdraftLimit;

    public CurrentAccount(String accountNumber, String name, int age,
                          double balance, String status, String pin,
                          double overdraftLimit) {
        super(accountNumber, name, age, balance, "CURRENT", status, pin);
        this.overdraftLimit = overdraftLimit;
    }

    public double getOverdraftLimit() {
        return overdraftLimit;
    }

    public void setOverdraftLimit(double overdraftLimit) {
        this.overdraftLimit = overdraftLimit;
    }


@Override
public boolean withdraw(double amount, String enteredPin)
        throws com.gdb.exceptions.InvalidPinException,
               com.gdb.exceptions.InactiveAccountException,
               com.gdb.exceptions.InvalidAmountException,
               com.gdb.exceptions.InsufficientBalanceException,
               com.gdb.exceptions.MinimumBalanceViolationException {

    if (!validatePin(enteredPin)) {
        throw new com.gdb.exceptions.InvalidPinException(
                "Invalid PIN entered");
    }

    if (!"ACTIVE".equals(getStatus())) {
        throw new com.gdb.exceptions.InactiveAccountException(
                "Account is not active");
    }

    if (amount <= 0) {
        throw new com.gdb.exceptions.InvalidAmountException(
                "Withdrawal amount must be positive");
    }

    if (amount > getBalance() + overdraftLimit) {
        throw new com.gdb.exceptions.InsufficientBalanceException(
                "Insufficient funds in account");
    }

updateBalanceForWithdrawal(amount);
    return true;
}





}
