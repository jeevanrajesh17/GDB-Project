
package com.gdb.tests;

import com.gdb.domain.AccountRulesEngine;

public class TestAccountRulesEngineProperties {

    public static void main(String[] args) {

        System.out.println(
                "=== Activity 14: Properties-Driven Rules Engine Test ===");

        int[] tenures = {0, 2, 4, 6};

        for (int tenure : tenures) {
            double minBalance =
                    AccountRulesEngine.getSavingsMinBalance(tenure);

            double interest =
                    AccountRulesEngine.getSavingsInterestRate(tenure);

            System.out.printf(
                    "Tenure %d yrs -> Min Balance: Rs %.1f | Interest: %.2f%%%n",
                    tenure, minBalance, interest);
        }

        // Verify external rules
        if (AccountRulesEngine.getSavingsMinBalance(0) != 10000.0
                || AccountRulesEngine.getSavingsMinBalance(2) != 7500.0
                || AccountRulesEngine.getSavingsMinBalance(4) != 5000.0
                || AccountRulesEngine.getSavingsMinBalance(6) != 2500.0
                || AccountRulesEngine.getSavingsInterestRate(4) != 3.5) {

            throw new AssertionError(
                    "External properties rules verification failed");
        }

        System.out.println(
                "All external properties loaded and verified successfully!");
    }
}
