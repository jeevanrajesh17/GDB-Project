
package com.gdb.tests;
import com.gdb.exceptions.InvalidAmountException;
import com.gdb.domain.AbstractAccount;
import com.gdb.domain.SavingsAccount;
import com.gdb.domain.CurrentAccount;
import com.gdb.domain.SalaryAccount;
import com.gdb.exceptions.AccountException;


public class TestAbstractAccount {

    public static void main(String[] args) throws Exception {

        SavingsAccount savings = new SavingsAccount(
                "SA001", "Rajesh", 28, 10000.0,
                "ACTIVE", "1234", 1000.0, 5.0);

        CurrentAccount current = new CurrentAccount(
                "CA001", "Arun", 30, 5000.0,
                "ACTIVE", "1234", 5000.0);

        SalaryAccount salary = new SalaryAccount(
                "SAL001", "Karthik", 27, 30000.0,
                "ACTIVE", "1234", "Infosys", 0);

        AbstractAccount[] accounts = {
            savings, current, salary
        };

        System.out.println("=== Activity 10: Banking Operations Suite ===");

        // Transfer Rs 3000 from Savings to Current.
        boolean transferSuccessful = transferFunds(
                savings, current, 3000.0, "1234");

        System.out.println("Transfer Rs 3000 from Savings to Current: "
                + (transferSuccessful ? "SUCCESS" : "FAILED"));

        System.out.println("Savings Balance: Rs " + savings.getBalance()
                + " | Current Balance: Rs " + current.getBalance());

        // Incorrect PIN: destination must not receive any money.
        double currentBalanceBefore = current.getBalance();

        boolean failedTransfer = transferFunds(
                savings, current, 1000.0, "9999");

        boolean balancesUnchanged =
                !failedTransfer
                && current.getBalance() == currentBalanceBefore;

        System.out.println("Failed Transfer (Wrong PIN): Exception caught, "
                + "no balance changed "
                + (balancesUnchanged ? "[PASS]" : "[FAIL]"));

        // Monthly banking cycle.
        for (AbstractAccount account : accounts) {
            if (account instanceof SavingsAccount) {
                ((SavingsAccount) account).applyInterest();
            } else if (account instanceof SalaryAccount) {
                SalaryAccount salaryAccount = (SalaryAccount) account;

                if (salaryAccount.checkSalaryCreditHistory()) {
                    System.out.println(
                            "Salary credit history checked: "
                            + salaryAccount.getAccountNumber());
                } else {
                    System.out.println(
                            "Salary account has inactive months: "
                            + salaryAccount.getAccountNumber());
                }
            }
        }

        System.out.println(
                "Monthly Interest Cycle processed for all qualifying accounts.");
        System.out.println("All banking operations passed!");
    }

private static boolean transferFunds(
        AbstractAccount source,
        AbstractAccount destination,
        double amount,
        String enteredPin) {

    try {
        source.withdraw(amount, enteredPin);
        destination.deposit(amount);
        return true;
    } catch (com.gdb.exceptions.AccountException e) {
        return false;
    }
}
}
