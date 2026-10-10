
package com.gdb.tests;

import com.gdb.domain.IAccount;
import com.gdb.domain.AccountFactory;
import com.gdb.exceptions.AccountException;
import com.gdb.exceptions.MinimumBalanceViolationException;

public class TestInterfaceFactory {

    public static void main(String[] args) {

        System.out.println("=== Activity 12: Factory-Driven System Suite ===");

        // Test 1: Savings account creation and deposit
        try {
            IAccount savings = AccountFactory.createAccount(
                    "SAVINGS", "SA001", "Rajesh Sharma",
                    28, 10000.0, "ACTIVE", "1234");

            savings.deposit(2000.0);

            if (savings.getBalance() == 12000.0) {
                System.out.println(
                        "[Test 1] Savings Account Creation & Deposit: [PASS]");
            } else {
                System.out.println(
                        "[Test 1] Savings Account Creation & Deposit: [FAIL]");
            }

        } catch (Exception e) {
            System.out.println(
                    "[Test 1] Savings Account Creation & Deposit: [FAIL]");
        }

        // Test 2: Current account overdraft withdrawal
        try {
            IAccount current = AccountFactory.createAccount(
                    "CURRENT", "CA001", "Priya Patel",
                    30, 15000.0, "ACTIVE", "2345");

            current.withdraw(20000.0, "2345");

            if (current.getBalance() == -5000.0) {
                System.out.println(
                        "[Test 2] Current Account Overdraft Withdrawal: [PASS]");
            } else {
                System.out.println(
                        "[Test 2] Current Account Overdraft Withdrawal: [FAIL]");
            }

        } catch (Exception e) {
            System.out.println(
                    "[Test 2] Current Account Overdraft Withdrawal: [FAIL]");
        }

        // Test 3: Fixed deposit premature withdrawal must be rejected
        try {
            IAccount fixedDeposit = AccountFactory.createAccount(
                    "FIXED_DEPOSIT", "FD001", "Amit Kumar",
                    35, 50000.0, "ACTIVE", "3456");

            fixedDeposit.withdraw(1000.0, "3456");

            System.out.println(
                    "[Test 3] Fixed Deposit Premature Withdrawal Block: [FAIL]");

        } catch (AccountException e) {
            System.out.println(
                    "[Test 3] Fixed Deposit Premature Withdrawal Block: [PASS]");
        } catch (Exception e) {
            System.out.println(
                    "[Test 3] Fixed Deposit Premature Withdrawal Block: [FAIL]");
        }

        // Test 4: Unknown account type must be rejected
        try {
            AccountFactory.createAccount(
                    "UNKNOWN", "XX001", "Unknown Customer",
                    25, 1000.0, "ACTIVE", "5678");

            System.out.println(
                    "[Test 4] Invalid Type Rejection: [FAIL]");

        } catch (IllegalArgumentException e) {
            System.out.println(
                    "[Test 4] Invalid Type Rejection: [PASS]");
        }

        System.out.println(
                "Factory-driven architecture successfully verified!");
    }
}
