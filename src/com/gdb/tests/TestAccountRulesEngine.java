
package com.gdb.tests;

import com.gdb.domain.AccountRulesEngine;

public class TestAccountRulesEngine {

    public static void main(String[] args) {
        System.out.println(
                "=== Activity 13.1: Hardcoded Rules Engine Test ===");

        int[] tenures = {0, 2, 4, 6};

        for (int tenure : tenures) {
            double minBalance =
                    AccountRulesEngine.getSavingsMinBalance(tenure);

            double interest =
                    AccountRulesEngine.getSavingsInterestRate(tenure);

            System.out.printf(
                    "Tenure %d yrs -> Min Balance: Rs %.1f | Interest: %.1f%%%n",
                    tenure, minBalance, interest);
        }

        // Verify current account overdraft calculation
        double overdraft =
                AccountRulesEngine.getCurrentOverdraftLimit(10000.0);

        if (overdraft != 25000.0) {
            throw new AssertionError("Minimum overdraft limit test failed");
        }

        double higherOverdraft =
                AccountRulesEngine.getCurrentOverdraftLimit(20000.0);

        if (higherOverdraft != 50000.0) {
            throw new AssertionError("Turnover-based overdraft test failed");
        }

        // Verify FD rate for a 12-month deposit
        double fdRate = AccountRulesEngine.getFDInterestRate(12);

        if (fdRate != 6.5) {
            throw new AssertionError("FD interest rate test failed");
        }

        System.out.println(
                "Rules Engine lookup completed successfully!");
    }
}
