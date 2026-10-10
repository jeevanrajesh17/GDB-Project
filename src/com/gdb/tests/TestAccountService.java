
package com.gdb.tests;

import com.gdb.domain.IAccount;
import com.gdb.domain.Transaction;
import com.gdb.logging.FileLogDestination;
import com.gdb.logging.TransactionLogger;
import com.gdb.service.AccountService;

public class TestAccountService {

    public static void main(String[] args) throws Exception {

        System.out.println("=== Activity 19: Account Service Test ===");

        TransactionLogger logger =
                new TransactionLogger(new FileLogDestination());

        logger.clear();

        AccountService service = new AccountService(logger);

        // 1. Open accounts
        IAccount john = service.openAccount(
                "SAVINGS", "John Doe", 25, 15000.0);

        IAccount jane = service.openAccount(
                "SAVINGS", "Jane Smith", 30, 10000.0);

        System.out.println("\n--- Accounts Created ---");
        john.displayAccountInfo();
        System.out.println();
        jane.displayAccountInfo();

        // 2. Deposit
        System.out.println("\n--- Deposit ---");
        Transaction deposit =
                service.deposit(1001, 5000.0);
        System.out.println(deposit);

        // 3. Withdraw
        System.out.println("\n--- Withdrawal ---");
        Transaction withdrawal =
                service.withdraw(1001, 2000.0, "1234");
        System.out.println(withdrawal);

        // 4. Transfer
        System.out.println("\n--- Transfer ---");
        Transaction transfer =
                service.transfer(1001, 1002, 1000.0, "1234");
        System.out.println(transfer);

        System.out.println("John's balance: Rs. "
                + john.getBalance());
        System.out.println("Jane's balance: Rs. "
                + jane.getBalance());

        // 5. Transaction history
        System.out.println("\n--- Transaction History ---");
        System.out.println("Total records: "
                + service.getTransactionHistory().size());

        service.getTransactionHistory().forEach(
                command -> System.out.println(command.getTransaction()));

        // 6. Exception handling
        System.out.println("\n--- Exception Handling ---");

        try {
            service.deposit(9999, 1000.0);
        } catch (Exception e) {
            System.out.println("Expected error: " + e.getMessage());
        }

        try {
            service.withdraw(1001, 500.0, "9999");
        } catch (Exception e) {
            System.out.println("Expected error: " + e.getMessage());
        }

        // 7. Close account
        System.out.println("\n--- Close Account ---");
        service.closeAccount(1002, "1234");
        System.out.println("Jane's account status: "
                + jane.getStatus());

        System.out.println("\nNext account number: "
                + service.getNextAccountNumber());

        System.out.println("\n=== Test Completed ===");
    }
}
