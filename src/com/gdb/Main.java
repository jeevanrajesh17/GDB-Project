
package com.gdb;

import com.gdb.logging.FileLogDestination;
import com.gdb.logging.TransactionLogger;
import com.gdb.service.AccountService;
import com.gdb.service.AccountUI;

public class Main {

    public static void main(String[] args) {

        System.out.println("==================================");
        System.out.println("   GDB BANKING SYSTEM");
        System.out.println("==================================");

        TransactionLogger logger =
                new TransactionLogger(new FileLogDestination());

        AccountService accountService =
                new AccountService(logger);

        AccountUI accountUI = new AccountUI(accountService);

        accountUI.start();
    }
}
