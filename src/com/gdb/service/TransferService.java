
package com.gdb.service;

import com.gdb.domain.Transaction;
import com.gdb.domain.TransactionType;
import java.time.LocalDateTime;
import com.gdb.domain.AbstractAccount;
import com.gdb.domain.IAccount;
import com.gdb.exceptions.AccountException;
import com.gdb.exceptions.InactiveAccountException;
import com.gdb.exceptions.InvalidAmountException;
import com.gdb.exceptions.InvalidPinException;
import com.gdb.exceptions.InsufficientBalanceException;

public class TransferService {

    public void transfer(
            IAccount from,
            IAccount to,
            double amount,
            String pin) throws AccountException {

        if (from == null || to == null) {
            throw new AccountException(
                "Source and destination accounts are required");
        }

        if (!"ACTIVE".equals(from.getStatus())
                || !"ACTIVE".equals(to.getStatus())) {
            throw new InactiveAccountException(
                "Both accounts must be active to transfer funds");
        }

        if (!(from instanceof AbstractAccount)
                || !(to instanceof AbstractAccount)) {
            throw new AccountException(
                "Unsupported account implementation");
        }

        AbstractAccount source = (AbstractAccount) from;

        if (!source.validatePin(pin)) {
            throw new InvalidPinException("Incorrect PIN");
        }

        if (amount <= 0) {
            throw new InvalidAmountException(
                "Transfer amount must be positive");
        }

        if (amount > from.getBalance()) {
            throw new InsufficientBalanceException(
                "Insufficient balance for transfer of Rs. " + amount);
        }

        source.resetDailyTransferIfNeeded();

        if (!source.canTransfer(amount)) {
            throw new AccountException(
                "Daily transfer limit exceeded. Remaining today: Rs. "
                + source.getRemainingDailyTransferLimit());
        }

        from.withdraw(amount, pin);
        to.deposit(amount);

        source.updateDailyTransferTotal(amount);
    }
    public Transaction transferWithTransaction(
        IAccount from,
        IAccount to,
        double amount,
        String pin) throws AccountException {

    transfer(from, to, amount, pin);

    return new Transaction(
        Transaction.generateId(),
        LocalDateTime.now(),
        Integer.parseInt(to.getAccountNumber()),
        TransactionType.TRANSFER,
        amount,
        from.getBalance(),
        "SUCCESS",
        "Transfer of Rs. " + amount
            + " to Account #" + to.getAccountNumber(),
        Integer.parseInt(from.getAccountNumber()),
        Integer.parseInt(to.getAccountNumber())
    );
}
}
