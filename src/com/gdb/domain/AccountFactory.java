
package com.gdb.domain;

public class AccountFactory {

    public static IAccount createAccount(
            String accountType,
            String accountNumber,
            String name,
            int age,
            double balance,
            String status,
            String pin) {

        if (accountType == null) {
            throw new IllegalArgumentException(
                    "Account type cannot be null");
        }

        switch (accountType.toUpperCase()) {

            case "SAVINGS":
                return new SavingsAccount(
                        accountNumber, name, age, balance,
                        status, pin, 1000.0, 5.0);

            case "CURRENT":
                return new CurrentAccount(
                        accountNumber, name, age, balance,
                        status, pin, 25000.0);

            case "FIXED_DEPOSIT":
            case "FD":
                return new FixedDepositAccount(
                        accountNumber, name, age, balance,
                        status, pin, 12, 6.5);

            case "SALARY":
                return new SalaryAccount(
                        accountNumber, name, age, balance,
                        status, pin, "Default Employer", 0);

            default:
                throw new IllegalArgumentException(
                        "Unknown account type: " + accountType);
        }
    }

    private AccountFactory() {
        // Prevent instantiation of this utility class.
    }
}
