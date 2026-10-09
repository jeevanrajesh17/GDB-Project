
package com.gdb.tests;

import com.gdb.domain.Account;
import com.gdb.exceptions.*;

public class TestAccountExceptions {

public static void main(String[] args) throws Exception {

        System.out.println("=== Activity 6: Exception Handling Suite ===");

        Account account = new Account(
                "ACC1001",
                "Rajesh Sharma",
                28,
                5000.0,
                "SAVINGS",
                "ACTIVE",
                "1234"
        );

        // Test 1: Invalid PIN Exception
        try {
            account.withdraw(1000.0, "9999");
        } catch (InvalidPinException e) {
            System.out.println("[Test 1] Caught Invalid PIN: "
                    + e.getMessage() + " [PASS]");
        }

        // Test 2: Inactive Account Exception
        account.suspend();

        try {
            account.withdraw(1000.0, "1234");
        } catch (InactiveAccountException e) {
            System.out.println("[Test 2] Caught Inactive Account: "
                    + e.getMessage() + " [PASS]");
        }

        account.activate();

        // Test 3: Invalid Amount Exception
        try {
            account.deposit(-500.0);
        } catch (InvalidAmountException e) {
            System.out.println("[Test 3] Caught Invalid Amount: "
                    + e.getMessage() + " [PASS]");
        }

        // Test 4: Insufficient Balance Exception
        try {
            account.withdraw(10000.0, "1234");
        } catch (InsufficientBalanceException e) {
            System.out.println("[Test 4] Caught Insufficient Funds: "
                    + e.getMessage() + " [PASS]");
        }

        // Test 5: Polymorphic catch using base exception
        account.close();

        try {
            account.withdraw(1000.0, "1234");
        } catch (AccountException e) {
            System.out.println("[Test 5] Polymorphic Handler caught: "
                    + e.getMessage() + " [PASS]");
        }

        System.out.println("All exception handling tests completed successfully!");
    }
}
