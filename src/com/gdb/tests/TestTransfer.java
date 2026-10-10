
package com.gdb.tests;

import com.gdb.domain.IAccount;
import com.gdb.domain.AccountFactory;
import com.gdb.domain.AbstractAccount;
import com.gdb.service.TransferService;
import com.gdb.exceptions.AccountException;

public class TestTransfer {

    public static void main(String[] args) {

        System.out.println("=== Activity 15: Transfer Service Test ===");

        IAccount acc1 = AccountFactory.createAccount(
            "SAVINGS", "1001", "Alice",
            25, 100000.0, "ACTIVE", "1234", 0
        );

        IAccount acc2 = AccountFactory.createAccount(
            "SAVINGS", "1002", "Bob",
            30, 20000.0, "ACTIVE", "5678", 0
        );

        AbstractAccount source = (AbstractAccount) acc1;
        TransferService service = new TransferService();

        try {
            service.transfer(acc1, acc2, 5000.0, "1234");

            System.out.println("Transfer successful!");
            System.out.println("Alice balance: Rs. " + acc1.getBalance());
            System.out.println("Bob balance: Rs. " + acc2.getBalance());

            try {
                service.transfer(acc1, acc2, 100000.0, "1234");
            } catch (AccountException e) {
                System.out.println(e.getMessage());
            }

            System.out.println(
                "Daily transfer limit: Rs. "
                + source.getDailyTransferLimit()
            );

            for (int i = 0; i < 3; i++) {
                try {
                    service.transfer(acc1, acc2, 20000.0, "1234");
                    System.out.println("Additional transfer successful.");
                } catch (AccountException e) {
                    System.out.println(e.getMessage());
                    break;
                }
            }

            System.out.println(
                "Total transferred today: Rs. "
                + source.getDailyTransferTotal()
            );

            System.out.println(
                "Remaining daily limit: Rs. "
                + source.getRemainingDailyTransferLimit()
            );

        } catch (AccountException e) {
            System.out.println("Transfer failed: " + e.getMessage());
        }
    }
}
