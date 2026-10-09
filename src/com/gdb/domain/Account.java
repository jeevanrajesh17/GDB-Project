
package com.gdb.domain;

public class Account {

    private String accountNumber;
    private String name;
    private int age;
    private double balance;
    private String accountType;
    private String status;
    private String pin;

    public Account(String accountNumber, String name, int age,
                   double balance, String accountType, String status,
                   String pin) {

        if (age < 18) {
            throw new IllegalArgumentException("Customer must be at least 18 years old.");
        }

        if (balance < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative.");
        }

        if (!isValidPin(pin)) {
            throw new IllegalArgumentException("PIN must contain exactly 4 digits.");
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

        this.pin = newPin;
        System.out.println("PIN Changed Successfully.");
        return true;
    }

    public boolean deposit(double amount) {
        if (amount <= 0) {
            return false;
        }

        balance += amount;
        return true;
    }

    public boolean withdraw(double amount, String enteredPin) {
        if (!validatePin(enteredPin)) {
            return false;
        }

        if (!"ACTIVE".equals(status)) {
            return false;
        }

        if (amount <= 0 || amount > balance) {
            return false;
        }

        balance -= amount;
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
            throw new IllegalArgumentException("Customer must be at least 18 years old.");
        }
        this.age = age;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        if (balance < 0) {
            throw new IllegalArgumentException("Balance cannot be negative.");
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
