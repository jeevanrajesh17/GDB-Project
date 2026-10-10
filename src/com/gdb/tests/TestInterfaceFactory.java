
package com.gdb.tests;

import com.gdb.domain.IAccount;
import com.gdb.domain.AccountFactory;

public class TestInterfaceFactory {

    public static void main(String[] args) {

        System.out.println(
                "=== Activity 11: Interface & Factory Pattern Test ===");

        IAccount savings = AccountFactory.createAccount(
                "SAVINGS", "SA001", "Rajesh Sharma",
                28, 10000.0, "ACTIVE", "1234");

        IAccount current = AccountFactory.createAccount(
                "CURRENT", "CA001", "Priya Patel",
                30, 15000.0, "ACTIVE", "2345");

        IAccount fixedDeposit = AccountFactory.createAccount(
                "FIXED_DEPOSIT", "FD001", "Amit Kumar",
                35, 50000.0, "ACTIVE", "3456");

        IAccount salary = AccountFactory.createAccount(
                "SALARY", "SAL001", "Sneha Verma",
                27, 30000.0, "ACTIVE", "4567");

        System.out.println("Factory created: "
                + savings.getAccountType() + " account for "
                + savings.getName());

        System.out.println("Factory created: "
                + current.getAccountType() + " account for "
                + current.getName());

        System.out.println("Factory created: "
                + fixedDeposit.getAccountType() + " account for "
                + fixedDeposit.getName());

        System.out.println("Factory created: "
                + salary.getAccountType() + " account for "
                + salary.getName());

        System.out.println(
                "All accounts successfully created through AccountFactory!");
    }
}
