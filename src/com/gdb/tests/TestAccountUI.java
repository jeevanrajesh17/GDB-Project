
package com.gdb.tests;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.gdb.domain.IAccount;
import com.gdb.logging.FileLogDestination;
import com.gdb.logging.TransactionLogger;
import com.gdb.service.AccountService;
import com.gdb.service.AccountUI;

public class TestAccountUI {

    public static void main(String[] args) throws Exception {

        System.out.println("=== Activity 20: AccountUI Test ===");

        TransactionLogger logger =
                new TransactionLogger(new FileLogDestination());

        logger.clear();

        AccountService service = new AccountService(logger);

        // Prepare sample accounts for UI testing.
        IAccount john = service.openAccount(
                "SAVINGS", "John Doe", 25, 15000.0);

        IAccount jane = service.openAccount(
                "SAVINGS", "Jane Smith", 30, 10000.0);

        System.out.println("\n[PASS] Sample accounts created.");

        // Simulate user selecting Exit from the menu.
        String simulatedInput = "9\n";

        InputStream originalInput = System.in;

        try {
            System.setIn(new ByteArrayInputStream(
                    simulatedInput.getBytes()));

            AccountUI ui = new AccountUI(service);
            ui.start();

            System.out.println(
                    "\n[PASS] AccountUI started and exited successfully.");
        } finally {
            System.setIn(originalInput);
        }

        // Validate service endpoints.
        if (service.getAccount(1001) != null
                && service.getAccount(1002) != null) {
            System.out.println("[PASS] getAccount()");
        } else {
            System.out.println("[FAIL] getAccount()");
        }

        if (service.getAllAccounts().size() == 2) {
            System.out.println("[PASS] getAllAccounts()");
        } else {
            System.out.println("[FAIL] getAllAccounts()");
        }

        if (service.getNextAccountNumber() == 1003) {
            System.out.println("[PASS] getNextAccountNumber()");
        } else {
            System.out.println("[FAIL] getNextAccountNumber()");
        }

        System.out.println("\n=== Test Completed ===");
    }
}
