
package com.gdb.service;

import java.util.List;
import java.util.Scanner;

import com.gdb.command.TransactionCommand;
import com.gdb.domain.IAccount;
import com.gdb.domain.Transaction;

public class AccountUI {

    private final AccountService accountService;
    private final Scanner scanner;

    public AccountUI(AccountService accountService) {
        if (accountService == null) {
            throw new IllegalArgumentException(
                    "AccountService cannot be null");
        }

        this.accountService = accountService;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        boolean running = true;

        while (running) {
            displayMenu();

            int choice = readInt("Enter your choice: ");

            try {
                switch (choice) {
                    case 1:
                        openAccount();
                        break;
                    case 2:
                        closeAccount();
                        break;
                    case 3:
                        deposit();
                        break;
                    case 4:
                        withdraw();
                        break;
                    case 5:
                        transfer();
                        break;
                    case 6:
                        viewAccount();
                        break;
                    case 7:
                        viewAllAccounts();
                        break;
                    case 8:
                        viewTransactionHistory();
                        break;
                    case 9:
                        running = false;
                        System.out.println(
                                "Thank you for using GDB Banking System.");
                        break;
                    default:
                        System.out.println(
                                "Invalid choice. Please select 1-9.");
                }
            } catch (Exception e) {
                System.out.println("Operation failed: " + e.getMessage());
            }

            System.out.println();
        }
    }

    private void displayMenu() {
        System.out.println("==================================");
        System.out.println("       GDB BANKING SYSTEM");
        System.out.println("==================================");
        System.out.println("1. Open Account");
        System.out.println("2. Close Account");
        System.out.println("3. Deposit");
        System.out.println("4. Withdraw");
        System.out.println("5. Transfer");
        System.out.println("6. View Account");
        System.out.println("7. View All Accounts");
        System.out.println("8. View Transaction History");
        System.out.println("9. Exit");
        System.out.println("==================================");
    }

    private void openAccount() {
        System.out.println("\n--- Open Account ---");
        System.out.println("Types: SAVINGS, CURRENT, FIXED_DEPOSIT, SALARY");

        String type = readLine("Account type: ");
        String name = readLine("Customer name: ");
        int age = readInt("Customer age: ");
        double balance = readDouble("Initial balance: ");

        IAccount account = accountService.openAccount(
                type, name, age, balance);

        System.out.println("Account created successfully.");
        account.displayAccountInfo();
        System.out.println("Default PIN: 1234");
    }

    private void closeAccount() throws Exception {
        System.out.println("\n--- Close Account ---");

        int number = readInt("Account number: ");
        String pin = readLine("PIN: ");

        accountService.closeAccount(number, pin);
        System.out.println("Account closed successfully.");
    }

    private void deposit() throws Exception {
        System.out.println("\n--- Deposit ---");

        int number = readInt("Account number: ");
        double amount = readDouble("Deposit amount: ");

        Transaction transaction =
                accountService.deposit(number, amount);

        System.out.println(transaction);
    }

    private void withdraw() throws Exception {
        System.out.println("\n--- Withdraw ---");

        int number = readInt("Account number: ");
        double amount = readDouble("Withdrawal amount: ");
        String pin = readLine("PIN: ");

        Transaction transaction =
                accountService.withdraw(number, amount, pin);

        System.out.println(transaction);
    }

    private void transfer() throws Exception {
        System.out.println("\n--- Transfer ---");

        int from = readInt("From account number: ");
        int to = readInt("To account number: ");
        double amount = readDouble("Transfer amount: ");
        String pin = readLine("Sender PIN: ");

        Transaction transaction =
                accountService.transfer(from, to, amount, pin);

        System.out.println(transaction);
    }

    private void viewAccount() {
        System.out.println("\n--- View Account ---");

        int number = readInt("Account number: ");
        IAccount account = accountService.getAccount(number);

        if (account == null) {
            System.out.println("Account not found: " + number);
        } else {
            account.displayAccountInfo();
        }
    }

    private void viewAllAccounts() {
        System.out.println("\n--- All Accounts ---");

        List<IAccount> accounts = accountService.getAllAccounts();

        if (accounts.isEmpty()) {
            System.out.println("No accounts found.");
            return;
        }

        for (IAccount account : accounts) {
            account.displayAccountInfo();
            System.out.println("------------------------------");
        }
    }

    private void viewTransactionHistory() {
        System.out.println("\n--- Transaction History ---");

        List<TransactionCommand> history =
                accountService.getTransactionHistory();

        if (history.isEmpty()) {
            System.out.println("No transactions found.");
            return;
        }

        for (TransactionCommand command : history) {
            System.out.println(command.getTransaction());
        }
    }

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid whole number.");
            }
        }
    }

    private double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid amount.");
            }
        }
    }

    private String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
}
