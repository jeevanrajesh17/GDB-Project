package com.gdb.tests;
import com.gdb.exceptions.*;
import com.gdb.domain.Account;

public class TestAccount {

    private static void printResult(String testName, boolean passed) {
        System.out.println(testName + ": "
                + (passed ? "[PASS]" : "[FAIL]"));
    }

public static void main(String[] args) throws Exception {
        System.out.println("=== Activity 4: Enhanced Account Test Suite ===");

        // Test 1: Underage Customer Rejection
        boolean test1 = false;

        try {
            new Account("ACC1001", "Rajesh Sharma", 16,
                    5000.0, "SAVINGS", "ACTIVE", "1234");
        } catch (IllegalArgumentException e) {
            test1 = true;
        }

        printResult("Test 1 (Underage Customer Rejection)", test1);

        // Create a valid account for the remaining tests.
        Account account = new Account(
                "ACC1002", "Rajesh Sharma", 28,
                5000.0, "SAVINGS", "ACTIVE", "1234"
        );

        // Test 2: Wrong PIN Rejection
        boolean wrongPinRejected =
                !account.withdraw(1000.0, "9999")
                && account.getBalance() == 5000.0;

        printResult("Test 2 (Wrong PIN Rejection)", wrongPinRejected);

        // Test 3: Correct PIN Withdrawal
        boolean correctPinAccepted =
                account.withdraw(1000.0, "1234")
                && account.getBalance() == 4000.0;

        printResult("Test 3 (Correct PIN Withdrawal)", correctPinAccepted);

        // Test 4: PIN Change and Old PIN Invalidation
        boolean pinChanged = account.changePin("1234", "5678");
        boolean oldPinRejected =
                !account.withdraw(500.0, "1234")
                && account.getBalance() == 4000.0;

        boolean test4 = pinChanged && oldPinRejected;

        printResult("Test 4 (PIN Change & Old PIN Invalidation)", test4);

        // Test 5: Suspended Account Withdrawal Block
        account.suspend();

        boolean test5 =
                !account.withdraw(500.0, "5678")
                && account.getBalance() == 4000.0;

        printResult("Test 5 (Suspended Account Block)", test5);

        // Test 6: Reactivation and Successful Withdrawal
        account.activate();

        boolean test6 =
                account.withdraw(1000.0, "5678")
                && account.getBalance() == 3000.0;

        printResult("Test 6 (Reactivation & Success)", test6);

        boolean allPassed =
                test1 && wrongPinRejected && correctPinAccepted
                && test4 && test5 && test6;

        if (allPassed) {
            System.out.println("All Enhanced Account tests passed!");
        } else {
            System.out.println("Some Enhanced Account tests failed.");
        }
    }
}
