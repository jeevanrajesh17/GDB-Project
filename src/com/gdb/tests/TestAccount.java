
package com.gdb.tests;

import com.gdb.domain.Account;

public class TestAccount {

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

        System.out.println("=== Activity 3: Enhanced Account Test ===");
        System.out.println("Initial Balance: Rs " + account.getBalance()
                + " | Status: " + account.getStatus());

        boolean withdrawal = account.withdraw(1000.0, "1234");
        System.out.println("Withdraw without PIN: "
                + (withdrawal ? "SUCCESS" : "FAILED")
                + " | Balance: Rs " + account.getBalance());

        boolean wrongPin = account.withdraw(1000.0, "9999");
        System.out.println("Withdraw with wrong PIN: "
                + (wrongPin ? "SUCCESS" : "FAILED")
                + " | Balance: Rs " + account.getBalance());

        account.suspend();

        boolean suspendedWithdrawal = account.withdraw(1000.0, "1234");
        System.out.println("Withdraw on SUSPENDED account: "
                + (suspendedWithdrawal ? "SUCCESS" : "FAILED")
                + " | Balance: Rs " + account.getBalance());

        account.activate();

        account.changePin("1234", "5678");

        boolean newPinWithdrawal = account.withdraw(1000.0, "5678");
        System.out.println("Withdraw with new PIN: "
                + (newPinWithdrawal ? "SUCCESS" : "FAILED")
                + " | Balance: Rs " + account.getBalance());
    }
}
