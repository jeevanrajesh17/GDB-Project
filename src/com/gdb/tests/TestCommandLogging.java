
package com.gdb.tests;

import com.gdb.command.DepositCommand;
import com.gdb.command.WithdrawCommand;
import com.gdb.command.TransferCommand;
import com.gdb.command.TransactionCommand;
import com.gdb.domain.AccountFactory;
import com.gdb.domain.AbstractAccount;
import com.gdb.domain.IAccount;
import com.gdb.logging.TransactionLog;

import java.util.List;

public class TestCommandLogging {

    public static void main(String[] args) throws Exception {

        System.out.println("============================================================");
        System.out.println("  ACTIVITY 17 — COMMAND PATTERN + FILE LOGGING");
        System.out.println("============================================================");

        TransactionLog log = new TransactionLog();
        log.clear();

        IAccount acc1 = AccountFactory.createAccount(
            "SAVINGS", "1001", "Rajesh Sharma",
            30, 15000.0, "ACTIVE", "1234", 0
        );

        IAccount acc2 = AccountFactory.createAccount(
            "SAVINGS", "1002", "Priya Sharma",
            28, 10000.0, "ACTIVE", "5678", 0
        );

        AbstractAccount source = (AbstractAccount) acc1;

        // STEP 10: Create and execute deposit command
        TransactionCommand deposit = new DepositCommand(source, 5000.0);
        deposit.execute();
        log.log(deposit);

        System.out.println(
            "\n[STEP 11] Logged: " + deposit.getTransaction()
        );

        // STEP 12: Create and execute withdrawal command
        TransactionCommand withdrawal =
            new WithdrawCommand(source, 2000.0, 1234);
        withdrawal.execute();
        log.log(withdrawal);

        System.out.println(
            "[STEP 12] Logged: " + withdrawal.getTransaction()
        );

        // STEP 13: Create and execute transfer command
        TransactionCommand transfer =
            new TransferCommand(acc1, acc2, 3000.0, "1234");
        transfer.execute();
        log.log(transfer);

        System.out.println(
            "[STEP 13] Logged: " + transfer.getTransaction()
        );

        // STEP 14: Read the log
        List<TransactionCommand> history = log.readAll();

        System.out.println(
            "\n[STEP 14] Read " + history.size()
            + " commands from transaction log:"
        );

        int count = 1;
        for (TransactionCommand command : history) {
            System.out.println(
                "  [" + count++ + "] " + command.getTransaction()
            );
        }

        if (history.size() != 3) {
            throw new AssertionError(
                "Expected 3 commands, found " + history.size()
            );
        }

        // STEP 15: Verify persistence with a new reader instance
        TransactionLog freshLog = new TransactionLog();
        List<TransactionCommand> persisted = freshLog.readAll();

        if (persisted.size() != 3) {
            throw new AssertionError(
                "Persistence check failed. Found "
                + persisted.size() + " commands"
            );
        }

        System.out.println(
            "\n[STEP 15] Fresh reader verified "
            + persisted.size() + " persisted commands [PASS]"
        );
    }
}
