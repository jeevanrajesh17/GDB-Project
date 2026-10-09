
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
    }
}