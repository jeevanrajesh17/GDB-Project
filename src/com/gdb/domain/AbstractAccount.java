
package com.gdb.domain;

import java.time.LocalDateTime;
import com.gdb.exceptions.AccountException;
import com.gdb.exceptions.InvalidAmountException;
import com.gdb.exceptions.InvalidPinException;
import com.gdb.exceptions.InactiveAccountException;
import java.time.LocalDateTime;

public abstract class AbstractAccount implements IAccount {

    protected String accountNumber;
    protected String name;
    protected int age;
    protected double balance;
    protected String accountType;
    protected String status;
    protected String pin;
    protected double dailyTransferTotal = 0.0;
protected LocalDateTime lastTransferDate = LocalDateTime.now();

    public AbstractAccount(String accountNumber, String name, int age,
                           double balance, String accountType,
                           String status, String pin) {

        if (age < 18) {
            throw new IllegalArgumentException(
                    "Customer must be at least 18 years old.");
        }

        if (balance < 0) {
            throw new IllegalArgumentException(
                    "Initial balance cannot be negative.");
        }

        if (pin == null || !pin.matches("\\d{4}")) {
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

    public boolean withdraw(double amount, String enteredPin)
            throws AccountException,
                   InvalidPinException,
                   InactiveAccountException,
                   InvalidAmountException {

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

        processDebit(amount);
        return true;
    }

    protected abstract void processDebit(double amount)
            throws AccountException;

    public boolean deposit(double amount) throws InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException(
                    "Deposit amount must be positive");
        }

        balance += amount;
        return true;
    }

    public boolean validatePin(String inputPin) {
        return pin != null && pin.equals(inputPin);
    }

    public boolean changePin(String oldPin, String newPin) {
        if (!validatePin(oldPin)
                || newPin == null
                || !newPin.matches("\\d{4}")) {
            return false;
        }

        pin = newPin;
        System.out.println("PIN Changed Successfully.");
        return true;
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

    public void setStatus(String status) {
        this.status = status;
    }

public double getDailyTransferTotal() {
    resetDailyTransferIfNeeded();
    return dailyTransferTotal;
}

public LocalDateTime getLastTransferDate() {
    return lastTransferDate;
}

public double getDailyTransferLimit() {
    return AccountRulesEngine.getInstance()
            .getDailyTransferLimit(getAccountType(), getTenureYears());
}

public double getRemainingDailyTransferLimit() {
    resetDailyTransferIfNeeded();
    return Math.max(0.0,
            getDailyTransferLimit() - dailyTransferTotal);
}

public boolean canTransfer(double amount) {
    resetDailyTransferIfNeeded();
    return amount > 0
            && dailyTransferTotal + amount <= getDailyTransferLimit();
}

public void updateDailyTransferTotal(double amount) {
    resetDailyTransferIfNeeded();
    dailyTransferTotal += amount;
    lastTransferDate = LocalDateTime.now();
}

public void resetDailyTransferIfNeeded() {
    if (lastTransferDate == null
            || !lastTransferDate.toLocalDate()
                    .equals(LocalDateTime.now().toLocalDate())) {
        dailyTransferTotal = 0.0;
        lastTransferDate = LocalDateTime.now();
    }
}

// Default tenure for account types without a tenure field.
public int getTenureYears() {
    return 0;
}
public Transaction depositWithTransaction(double amount)
        throws InvalidAmountException {

    deposit(amount);

    return new Transaction(
        Transaction.generateId(),
        LocalDateTime.now(),
        Integer.parseInt(getAccountNumber()),
        TransactionType.DEPOSIT,
        amount,
        getBalance(),
        "SUCCESS",
        "Deposit of Rs. " + amount,
        0,
        Integer.parseInt(getAccountNumber())
    );
}
public Transaction withdrawWithTransaction(double amount, int pin)
        throws AccountException {

    withdraw(amount, String.valueOf(pin));

    return new Transaction(
        Transaction.generateId(),
        LocalDateTime.now(),
        Integer.parseInt(getAccountNumber()),
        TransactionType.WITHDRAW,
        amount,
        getBalance(),
        "SUCCESS",
        "Withdrawal of Rs. " + amount,
        Integer.parseInt(getAccountNumber()),
        0
    );
}

}
