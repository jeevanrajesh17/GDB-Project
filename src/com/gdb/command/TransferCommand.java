
package com.gdb.command;

import com.gdb.domain.IAccount;
import com.gdb.domain.Transaction;
import com.gdb.service.TransferService;

public class TransferCommand implements TransactionCommand {

    private static final long serialVersionUID = 1L;

    private final transient IAccount from;
    private final transient IAccount to;
    private final double amount;
    private final String pin;
    private Transaction transaction;

    public TransferCommand(
            IAccount from,
            IAccount to,
            double amount,
            String pin) {
        this.from = from;
        this.to = to;
        this.amount = amount;
        this.pin = pin;
    }

    @Override
    public void execute() throws Exception {
        TransferService service = new TransferService();
        transaction = service.transferWithTransaction(
            from, to, amount, pin
        );
    }

    @Override
    public Transaction getTransaction() {
        return transaction;
    }
}
