
package com.gdb.tests;

import com.gdb.domain.Account;

public class TestAccount {

    public static void main(String[] args) {

        Account account = new Account(
            "ACC1001",
            "Rajesh Sharma",
            28,
            5000.0,
            "SAVINGS",
            "ACTIVE"
        );

        System.out.println("=== Activity 2: Test Account Suite ===");

        // Test 1: Initial balance
        boolean test1 = account.getBalance() == 5000.0;
        printResult("Test 1 (Initial Balance 5000.0)", test1);

        // Test 2: Valid deposit
        boolean depositResult = account.deposit(2000.0);
        boolean test2 = depositResult && account.getBalance() == 7000.0;
        printResult("Test 2 (Deposit 2000.0 -> Balance 7000.0)", test2);

        // Test 3: Negative deposit
        boolean negativeDepositResult = account.deposit(-500.0);
        boolean test3 = !negativeDepositResult
                && account.getBalance() == 7000.0;
        printResult("Test 3 (Negative Deposit -> Rejected)", test3);

        // Test 4: Valid withdrawal
        boolean withdrawalResult = account.withdraw(3000.0);
        boolean test4 = withdrawalResult && account.getBalance() == 4000.0;
        printResult("Test 4 (Withdraw 3000.0 -> Balance 4000.0)", test4);

        // Test 5: Withdrawal exceeding balance
        boolean excessiveWithdrawalResult = account.withdraw(10000.0);
        boolean test5 = !excessiveWithdrawalResult
                && account.getBalance() == 4000.0;
        printResult("Test 5 (Exceeding Withdrawal -> Rejected)", test5);

        // Test 6: Negative withdrawal
        boolean negativeWithdrawalResult = account.withdraw(-100.0);
        boolean test6 = !negativeWithdrawalResult
                && account.getBalance() == 4000.0;
        printResult("Test 6 (Negative Withdrawal -> Rejected)", test6);

        if (test1 && test2 && test3 && test4 && test5 && test6) {
            System.out.println("All Account tests completed successfully!");
        } else {
            System.out.println("Some Account tests failed.");
        }
    }

    private static void printResult(String testName, boolean passed) {
        System.out.println(testName + ": "
                + (passed ? "[PASS]" : "[FAIL]"));
    }
}
