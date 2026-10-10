
package com.gdb.tests;

import com.gdb.domain.AccountFactory;
import com.gdb.domain.IAccount;
import com.gdb.domain.SavingsAccount;
import com.gdb.domain.Transaction;
import com.gdb.service.TransferService;

public class TestTransactionModel {

    public static void main(String[] args) throws Exception {

        System.out.println("============================================================");
        System.out.println("  ACTIVITY 16 — TRANSACTION MODEL TEST");
        System.out.println("============================================================");

        IAccount acc1 = AccountFactory.createAccount(
            "SAVINGS", "1001", "Rajesh Sharma",
            30, 50000.0, "ACTIVE", "1234", 0
        );

        IAccount acc2 = AccountFactory.createAccount(
            "SAVINGS", "1002", "Priya Sharma",
            28, 20000.0, "ACTIVE", "5678", 0
        );

        SavingsAccount account = (SavingsAccount) acc1;

        // STEP 10: Deposit transaction
        Transaction depositTransaction =
            account.depositWithTransaction(5000.0);

        System.out.println(
            "\n[STEP 10] Deposit Transaction: " + depositTransaction
        );

        // STEP 11: Withdrawal transaction
        Transaction withdrawalTransaction =
            account.withdrawWithTransaction(2000.0, 1234);

        System.out.println(
            "[STEP 11] Withdrawal Transaction: " + withdrawalTransaction
        );

        // STEP 12: Transfer transaction
        TransferService service = new TransferService();

        Transaction transferTransaction =
            service.transferWithTransaction(
                acc1, acc2, 1000.0, "1234"
            );

        System.out.println(
            "[STEP 12] Transfer Transaction: " + transferTransaction
        );

        // STEP 13: Verify legacy deposit compatibility
        acc1.deposit(1000.0);

        System.out.println("[STEP 13] Legacy Deposit +1000:");
        acc1.displayAccountInfo();
    }
}
