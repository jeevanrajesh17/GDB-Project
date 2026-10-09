
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
            "ACTIVE"
        );

        System.out.println("=== Activity 1: Basic Account Test ===");

        account.displayAccountInfo();

        boolean depositResult = account.deposit(2000);
        System.out.println("Deposit 2000: "
                + (depositResult ? "SUCCESS" : "FAILED")
                + " | Balance: Rs " + account.getBalance());

        boolean withdrawResult = account.withdraw(3000);
        System.out.println("Withdraw 3000: "
                + (withdrawResult ? "SUCCESS" : "FAILED")
                + " | Balance: Rs " + account.getBalance());

        boolean invalidWithdraw = account.withdraw(10000);
        System.out.println("Withdraw 10000 (exceeds balance): "
                + (invalidWithdraw ? "SUCCESS" : "FAILED")
                + " | Balance: Rs " + account.getBalance());
    }
}
