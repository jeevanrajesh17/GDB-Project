
package com.gdb;

import com.gdb.domain.IAccount;
import com.gdb.logging.FileLogDestination;
import com.gdb.logging.TransactionLogger;
import com.gdb.service.AccountService;

public class Main {

    public static void main(String[] args) throws Exception {

        System.out.println("==================================");
        System.out.println("   GDB BANKING SYSTEM");
        System.out.println("==================================");

        TransactionLogger logger =
        new TransactionLogger(new FileLogDestination());

logger.clear();

AccountService accountService =
        new AccountService(logger);

        IAccount john = accountService.openAccount(
                "SAVINGS", "John Doe", 25, 15000.0);

        IAccount jane = accountService.openAccount(
                "SAVINGS", "Jane Smith", 30, 10000.0);

        System.out.println("\n--- Initial Accounts ---");
        john.displayAccountInfo();
        System.out.println();
        jane.displayAccountInfo();

        System.out.println("\n--- Banking Operations ---");

        System.out.println("\nDeposit:");
        System.out.println(accountService.deposit(1001, 5000.0));

        System.out.println("\nWithdrawal:");
        System.out.println(accountService.withdraw(1001, 2000.0, "1234"));

        System.out.println("\nTransfer:");
        System.out.println(accountService.transfer(
                1001, 1002, 1000.0, "1234"));

        System.out.println("\n--- Final Balances ---");
        System.out.println("John Doe: Rs. " + john.getBalance());
        System.out.println("Jane Smith: Rs. " + jane.getBalance());

        System.out.println("\n--- Transaction History ---");
        accountService.getTransactionHistory().forEach(
                command -> System.out.println(command.getTransaction()));

        System.out.println("\n==================================");
        System.out.println("   APPLICATION COMPLETED");
        System.out.println("==================================");
    }
}
