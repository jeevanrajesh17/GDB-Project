package com.gdb.tests;

import com.gdb.domain.Account;
import com.gdb.exceptions.*;

public class TestAccountExceptions {

    public static void main(String[] args) {

        Account account = new Account(
                "ACC1001",
                "Rajesh Sharma",
                28,
                5000.0,
                "SAVINGS",
                "ACTIVE",
                "1234"
        );

        // Test 1: Invalid PIN
        try {
            account.withdraw(1000.0, "9999");
        } catch (InvalidPinException e) {
            System.out.println(
                    "Caught InvalidPinException as expected: "
                    + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected exception: " + e.getMessage());
        }

        // Test 2: Inactive account
        account.suspend();

        try {
            account.withdraw(1000.0, "1234");
        } catch (InactiveAccountException e) {
            System.out.println(
                    "Caught InactiveAccountException as expected: "
                    + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected exception: " + e.getMessage());
        }

        account.activate();

        // Test 3: Invalid amount
        try {
            account.withdraw(-100.0, "1234");
        } catch (InvalidAmountException e) {
            System.out.println(
                    "Caught InvalidAmountException as expected: "
                    + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected exception: " + e.getMessage());
        }

        // Test 4: Insufficient balance
        try {
            account.withdraw(6000.0, "1234");
        } catch (InsufficientBalanceException e) {
            System.out.println(
                    "Caught InsufficientBalanceException as expected: "
                    + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected exception: " + e.getMessage());
        }

        // Test 5: Successful withdrawal
        try {
            account.withdraw(1000.0, "1234");
            System.out.println(
                    "Successful withdrawal completed: Rs 1000.0"
                    + " | New Balance: Rs "
                    + account.getBalance());
        } catch (Exception e) {
            System.out.println("Unexpected exception: " + e.getMessage());
        }
    }
}
