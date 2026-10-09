
package com.gdb.tests;

import com.gdb.domain.SavingsAccount;
import com.gdb.domain.CurrentAccount;
import com.gdb.domain.FixedDepositAccount;
import com.gdb.exceptions.MinimumBalanceViolationException;
import com.gdb.exceptions.InsufficientBalanceException;
import com.gdb.exceptions.AccountException;

public class TestAbstractAccount {

    public static void main(String[] args) {
        System.out.println("=== Activity 9: Abstract Account & Template Pattern ===");

        SavingsAccount savings = new SavingsAccount(
                "SA001", "Rajesh", 28, 10000.0,
                "ACTIVE", "1234", 1000.0, 5.0);

        try {
            savings.withdraw(2000.0, "1234");
            System.out.println("[Savings] Withdraw 2000: SUCCESS | Balance: Rs "
                    + savings.getBalance());
        } catch (Exception e) {
            System.out.println("[Savings] Unexpected exception: " + e.getMessage());
        }

        try {
            savings.withdraw(8000.0, "1234");
            System.out.println("[Savings] Withdraw below min balance: FAILED");
        } catch (MinimumBalanceViolationException e) {
            System.out.println("[Savings] Withdraw below min balance: Caught "
                    + "MinimumBalanceViolationException [PASS]");
        } catch (Exception e) {
            System.out.println("[Savings] Unexpected exception: " + e.getMessage());
        }

        CurrentAccount current = new CurrentAccount(
                "CA001", "Arun", 30, 2000.0,
                "ACTIVE", "1234", 5000.0);

        try {
            current.withdraw(5000.0, "1234");
            System.out.println("[Current] Overdraft debit: SUCCESS | Balance: Rs "
                    + current.getBalance());
        } catch (Exception e) {
            System.out.println("[Current] Unexpected exception: " + e.getMessage());
        }

        FixedDepositAccount fixedDeposit = new FixedDepositAccount(
                "FD001", "Kumar", 35, 50000.0,
                "ACTIVE", "1234", 12, 6.5);

        try {
            fixedDeposit.withdraw(1000.0, "1234");
            System.out.println("[FixedDeposit] Premature debit: FAILED");
        } catch (AccountException e) {
            System.out.println("[FixedDeposit] Premature debit: Caught "
                    + "AccountException [PASS]");
        } catch (Exception e) {
            System.out.println("[FixedDeposit] Unexpected exception: " + e.getMessage());
        }

        System.out.println("Template method pattern executed successfully!");
    }
}
