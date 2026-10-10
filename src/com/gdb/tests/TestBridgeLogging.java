
package com.gdb.tests;

import com.gdb.command.DepositCommand;
import com.gdb.command.TransactionCommand;
import com.gdb.domain.AccountFactory;
import com.gdb.domain.AbstractAccount;
import com.gdb.domain.IAccount;
import com.gdb.logging.DatabaseLogDestination;
import com.gdb.logging.FileLogDestination;
import com.gdb.logging.MemoryLogDestination;
import com.gdb.logging.SimulatedDatabase;
import com.gdb.logging.TransactionLogger;

import java.util.List;

public class TestBridgeLogging {

    public static void main(String[] args) throws Exception {

        System.out.println("============================================================");
        System.out.println("  ACTIVITY 18 — BRIDGE PATTERN (FILE + DB)");
        System.out.println("============================================================");

        // Create three independent destinations
        FileLogDestination fileDestination = new FileLogDestination();

        SimulatedDatabase database = new SimulatedDatabase();
        DatabaseLogDestination databaseDestination =
                new DatabaseLogDestination(database);

        MemoryLogDestination memoryDestination =
                new MemoryLogDestination();

        // Clear previous data
        fileDestination.clear();
        databaseDestination.clear();
        memoryDestination.clear();

        // Create test accounts
        IAccount acc1 = AccountFactory.createAccount(
            "SAVINGS", "1001", "Rajesh Sharma",
            30, 50000.0, "ACTIVE", "1234", 0
        );

        AbstractAccount account = (AbstractAccount) acc1;

        // Create three commands
        TransactionCommand deposit1 =
                new DepositCommand(account, 1000.0);
        TransactionCommand deposit2 =
                new DepositCommand(account, 2000.0);
        TransactionCommand deposit3 =
                new DepositCommand(account, 3000.0);

        deposit1.execute();
        deposit2.execute();
        deposit3.execute();

        TransactionLogger logger =
                new TransactionLogger(fileDestination);

        // STEP 25: File backend
        System.out.println("\n[STEP 25] Logging to FILE destination...");
        logger.log(deposit1);
        logger.log(deposit2);
        logger.log(deposit3);

        System.out.println(
            "  FILE log count: " + logger.readAll().size()
        );

        // STEP 26: Database backend
        logger.setDestination(databaseDestination);

        System.out.println(
            "\n[STEP 26] Switched to DATABASE destination..."
        );

        logger.log(deposit1);
        logger.log(deposit2);
        logger.log(deposit3);

        System.out.println(
            "  DATABASE log count: " + logger.readAll().size()
        );

        // STEP 27: Memory backend
        logger.setDestination(memoryDestination);

        System.out.println(
            "\n[STEP 27] Switched to MEMORY destination..."
        );

        logger.log(deposit1);
        logger.log(deposit2);
        logger.log(deposit3);

        System.out.println(
            "  MEMORY log count: " + logger.readAll().size()
        );

        // STEP 28: Verify data isolation
        logger.setDestination(fileDestination);
        List<TransactionCommand> fileRecords = logger.readAll();

        logger.setDestination(databaseDestination);
        List<TransactionCommand> databaseRecords = logger.readAll();

        logger.setDestination(memoryDestination);
        List<TransactionCommand> memoryRecords = logger.readAll();

        System.out.println("\n[STEP 28] Verifying Data Isolation:");
        System.out.println(
            "  FILE count: " + fileRecords.size()
            + " [EXPECTED: 3]"
        );
        System.out.println(
            "  DATABASE count: " + databaseRecords.size()
            + " [EXPECTED: 3]"
        );
        System.out.println(
            "  MEMORY count: " + memoryRecords.size()
            + " [EXPECTED: 3]"
        );

        if (fileRecords.size() != 3
                || databaseRecords.size() != 3
                || memoryRecords.size() != 3) {
            throw new AssertionError(
                "Data isolation verification failed"
            );
        }

        System.out.println(
            "\n[STEP 29] All Bridge Pattern log backends "
            + "verified successfully!"
        );
    }
}
