
package com.gdb.tests;

import com.gdb.domain.*;

public class TestAccountSubclasses {

    public static void main(String[] args) {

        System.out.println("=== Activity 7: Account Subclasses Test ===");

        SavingsAccount savings = new SavingsAccount(
                "SAV001", "Rajesh Sharma", 28,
                10000.0, "ACTIVE", "1234",
                1000.0, 4.0);

        System.out.println("Savings Account Created: Balance Rs "
                + savings.getBalance()
                + " | Min Balance: Rs " + savings.getMinBalance());

        CurrentAccount current = new CurrentAccount(
                "CUR001", "Priya Kumar", 30,
                20000.0, "ACTIVE", "1234",
                25000.0);

        System.out.println("Current Account Created: Overdraft Limit Rs "
                + current.getOverdraftLimit());

        FixedDepositAccount fixedDeposit = new FixedDepositAccount(
                "FD001", "Arun Kumar", 35,
                50000.0, "ACTIVE", "1234",
                12, 6.5);

        System.out.println("Fixed Deposit Created: Tenure "
                + fixedDeposit.getTenureMonths()
                + " months | Interest: " + fixedDeposit.getInterestRate() + "%");

        SalaryAccount salary = new SalaryAccount(
                "SAL001", "Karthik", 27,
                30000.0, "ACTIVE", "1234",
                "Infosys", 0);

        System.out.println("Salary Account Created: Employer "
                + salary.getEmployerName());

        System.out.println("All subclasses instantiated successfully!");

        System.out.println("\n=== Activity 8: Polymorphism Test ===");

        // Test 1: Savings account minimum balance
        try {
            savings.withdraw(9500.0, "1234");
            System.out.println("[Savings] Test FAILED");
        } catch (com.gdb.exceptions.MinimumBalanceViolationException e) {
            System.out.println(
                "[Savings] Withdraw 9500 (breaches min balance 1000): "
                + "Caught MinimumBalanceViolationException [PASS]");
        } catch (Exception e) {
            System.out.println("[Savings] Unexpected exception: " + e.getMessage());
        }

        // Test 2: Current account withdrawal within overdraft limit
        try {
            current.withdraw(40000.0, "1234");
            System.out.println(
                "[Current] Withdraw within overdraft: Balance Rs "
                + current.getBalance() + " [PASS]");
        } catch (Exception e) {
            System.out.println("[Current] Unexpected exception: " + e.getMessage());
        }

        // Test 3: Current account withdrawal beyond overdraft limit
        try {
            current.withdraw(10000.0, "1234");
            System.out.println("[Current] Overdraft test FAILED");
        } catch (com.gdb.exceptions.InsufficientBalanceException e) {
            System.out.println(
                "[Current] Withdraw exceeding overdraft: "
                + "Caught InsufficientBalanceException [PASS]");
        } catch (Exception e) {
            System.out.println("[Current] Unexpected exception: " + e.getMessage());
        }

        // Test 4: Fixed deposit premature withdrawal
        try {
            fixedDeposit.withdraw(1000.0, "1234");
            System.out.println("[FixedDeposit] Test FAILED");
        } catch (com.gdb.exceptions.AccountException e) {
            System.out.println(
                "[FixedDeposit] Withdraw attempt: "
                + "Caught AccountException [PASS]");
        } catch (Exception e) {
            System.out.println("[FixedDeposit] Unexpected exception: " + e.getMessage());
        }

        System.out.println("All polymorphic behaviors verified!");

    }
}