
package com.gdb.domain;

import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public final class AccountRulesEngine {

    private static final NavigableMap<Integer, Double> SAVINGS_MIN_BALANCE =
            new TreeMap<>();

    private static final NavigableMap<Integer, Double> SAVINGS_INTEREST_RATE =
            new TreeMap<>();

    private static final NavigableMap<Integer, Double> FD_INTEREST_RATE =
            new TreeMap<>();

    static {
        // Savings minimum balance by tenure
        SAVINGS_MIN_BALANCE.put(0, 10000.0);
        SAVINGS_MIN_BALANCE.put(1, 7500.0);
        SAVINGS_MIN_BALANCE.put(3, 5000.0);
        SAVINGS_MIN_BALANCE.put(5, 2500.0);

        // Savings interest rate by tenure
        SAVINGS_INTEREST_RATE.put(0, 2.70);
        SAVINGS_INTEREST_RATE.put(1, 3.00);
        SAVINGS_INTEREST_RATE.put(3, 3.50);
        SAVINGS_INTEREST_RATE.put(5, 4.00);

        // FD interest rate: 12 months or more
        FD_INTEREST_RATE.put(12, 6.5);
    }

    private AccountRulesEngine() {
        // Utility class: prevent object creation.
    }

    public static double getSavingsMinBalance(int tenureYears) {
        Map.Entry<Integer, Double> rule =
                SAVINGS_MIN_BALANCE.floorEntry(Math.max(0, tenureYears));

        return rule.getValue();
    }

    public static double getSavingsInterestRate(int tenureYears) {
        Map.Entry<Integer, Double> rule =
                SAVINGS_INTEREST_RATE.floorEntry(Math.max(0, tenureYears));

        return rule.getValue();
    }

    public static double getCurrentOverdraftLimit(double monthlyTurnover) {
        return Math.max(25000.0, monthlyTurnover * 2.5);
    }

    public static double getFDInterestRate(int months) {
        Map.Entry<Integer, Double> rule =
                FD_INTEREST_RATE.floorEntry(Math.max(0, months));

        return rule == null ? 0.0 : rule.getValue();
    }
}
