
package com.gdb.domain;

public final class AccountRulesEngine {

    private static final String RULES_PATH =
            "src/main/resources/config/rules/";

    private static final AccountRulesEngine INSTANCE =
            new AccountRulesEngine();

    private static final AccountRulesPropertiesLoader SAVINGS_RULES =
            new AccountRulesPropertiesLoader(
                    RULES_PATH + "savings.properties");

    private static final AccountRulesPropertiesLoader CURRENT_RULES =
            new AccountRulesPropertiesLoader(
                    RULES_PATH + "current.properties");

    private static final AccountRulesPropertiesLoader FD_RULES =
            new AccountRulesPropertiesLoader(
                    RULES_PATH + "fixeddeposit.properties");

    private static final AccountRulesPropertiesLoader SALARY_RULES =
            new AccountRulesPropertiesLoader(
                    RULES_PATH + "salary.properties");

    private AccountRulesEngine() {
        // Prevent external instantiation.
    }

    public static AccountRulesEngine getInstance() {
        return INSTANCE;
    }

    public static double getSavingsMinBalance(int tenureYears) {
        if (tenureYears >= 5) {
            return SAVINGS_RULES.getDouble(
                    "savings.minBalance.privilege", 2500.0);
        } else if (tenureYears >= 3) {
            return SAVINGS_RULES.getDouble(
                    "savings.minBalance.premium", 5000.0);
        } else if (tenureYears >= 1) {
            return SAVINGS_RULES.getDouble(
                    "savings.minBalance.standard", 7500.0);
        }

        return SAVINGS_RULES.getDouble(
                "savings.minBalance.new", 10000.0);
    }

    public static double getSavingsInterestRate(int tenureYears) {
        if (tenureYears >= 5) {
            return SAVINGS_RULES.getDouble(
                    "savings.interestRate.privilege", 4.00);
        } else if (tenureYears >= 3) {
            return SAVINGS_RULES.getDouble(
                    "savings.interestRate.premium", 3.50);
        } else if (tenureYears >= 1) {
            return SAVINGS_RULES.getDouble(
                    "savings.interestRate.standard", 3.00);
        }

        return SAVINGS_RULES.getDouble(
                "savings.interestRate.new", 2.70);
    }

    public static double getCurrentOverdraftLimit(double monthlyTurnover) {
        double minimum = CURRENT_RULES.getDouble(
                "current.overdraft.minimum", 25000.0);

        double multiplier = CURRENT_RULES.getDouble(
                "current.overdraft.turnoverMultiplier", 2.5);

        return Math.max(minimum, monthlyTurnover * multiplier);
    }

    public static double getFDInterestRate(int months) {
        double defaultRate = FD_RULES.getDouble(
                "fixeddeposit.interestRate.default", 5.0);

        double rate12Months = FD_RULES.getDouble(
                "fixeddeposit.interestRate.12months", 6.5);

        return months >= 12 ? rate12Months : defaultRate;
    }

    public static int getSalaryInactiveMonthsLimit() {
        return (int) SALARY_RULES.getDouble(
                "salary.inactiveMonths.limit", 3.0);
    }

    public static double getSalaryOverdraftLimit() {
        return SALARY_RULES.getDouble(
                "salary.overdraft.limit", 0.0);
    }

    public double getDailyTransferLimit(
            String accountType, int tenureYears) {

        Object limit = getAdditionalFeature(
                accountType, tenureYears, "dailyTransferLimit");

        return limit == null ? 0.0 : (Double) limit;
    }

    private Object getAdditionalFeature(
            String accountType, int tenureYears, String featureKey) {

        if (accountType == null) {
            return null;
        }

        AccountRulesPropertiesLoader loader;
        String type = accountType.replace("_", "")
                                 .replace(" ", "")
                                 .toUpperCase();

        switch (type) {
            case "SAVINGS":
                loader = SAVINGS_RULES;
                break;
            case "CURRENT":
                loader = CURRENT_RULES;
                break;
            case "FIXEDDEPOSIT":
            case "FD":
                loader = FD_RULES;
                break;
            case "SALARY":
                loader = SALARY_RULES;
                break;
            default:
                return null;
        }

        String bucket;
        if (tenureYears >= 5) {
            bucket = "privilege";
        } else if (tenureYears >= 3) {
            bucket = "premium";
        } else if (tenureYears >= 1) {
            bucket = "standard";
        } else {
            bucket = "new";
        }

        String key = featureKey + "." + bucket;

        // Property files use daily.transfer.limit.<bucket>.
        if ("dailyTransferLimit".equals(featureKey)) {
            key = "daily.transfer.limit." + bucket;
        }

        String value = loader.getProperty(key, null);

        if (value == null) {
            return null;
        }

        try {
            return Double.valueOf(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Invalid rule value for property: " + key, e);
        }
    }
}
