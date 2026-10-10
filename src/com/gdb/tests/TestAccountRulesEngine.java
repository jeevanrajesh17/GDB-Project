
package com.gdb.tests;

import com.gdb.domain.AccountFactory;
import com.gdb.domain.SavingsAccount;

public class TestAccountRulesEngine {

    public static void main(String[] args) {

        System.out.println(
                "=== Activity 13.2: Dynamic Account Rules Test ===");

        SavingsAccount account = (SavingsAccount)
                AccountFactory.createAccount(
                        "SAVINGS",
                        "SA013",
                        "Test Customer",
                        25,
                        20000.0,
                        "ACTIVE",
                        "1234",
                        4);

        System.out.println(
                "Created Savings Account (Tenure: "
                        + account.getTenureYears() + " yrs):");

        System.out.println(
                " -> Min Balance: Rs "
                        + account.getMinBalance()
                        + " (Dynamically fetched)");

        System.out.println(
                " -> Interest Rate: "
                        + account.getInterestRate()
                        + "% (Dynamically fetched)");

        if (account.getMinBalance() != 5000.0
                || account.getInterestRate() != 3.5) {
            throw new AssertionError(
                    "Dynamic rule assignment failed");
        }

        System.out.println(
                "Dynamic rule integration verified!");
    }
}
