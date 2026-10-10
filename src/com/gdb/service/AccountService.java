
package com.gdb.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.gdb.command.DepositCommand;
import com.gdb.command.TransactionCommand;
import com.gdb.command.TransferCommand;
import com.gdb.command.WithdrawCommand;
import com.gdb.domain.AbstractAccount;
import com.gdb.domain.AccountFactory;
import com.gdb.domain.IAccount;
import com.gdb.domain.Transaction;
import com.gdb.exceptions.AccountException;
import com.gdb.logging.TransactionLogger;

public class AccountService {

    private final Map<Integer, IAccount> accounts;
    private final TransactionLogger logger;
    private final TransferService transferService;

    private int nextAccountNumber = 1001;

    public AccountService(TransactionLogger logger) {
        this.accounts = new LinkedHashMap<>();
        this.logger = logger;
        this.transferService = new TransferService();

        if (logger == null) {
            throw new IllegalArgumentException(
                    "Transaction logger cannot be null");
        }
    }

    public IAccount openAccount(
            String type,
            String name,
            int age,
            double initialBalance) {

        int accountNumber = nextAccountNumber++;

        IAccount account = AccountFactory.createAccount(
                type,
                String.valueOf(accountNumber),
                name,
                age,
                initialBalance,
                "ACTIVE",
                "1234");

        accounts.put(accountNumber, account);
        return account;
    }

    public void closeAccount(int accountNumber, String pin)
            throws AccountException {

        IAccount account = getRequiredAccount(accountNumber);
        AbstractAccount abstractAccount = requireAbstractAccount(account);

        if (!abstractAccount.validatePin(pin)) {
            throw new AccountException("Incorrect PIN");
        }

        if (!"ACTIVE".equals(account.getStatus())) {
            throw new AccountException("Account is not active");
        }

        abstractAccount.setStatus("CLOSED");
    }

    public Transaction deposit(int accountNumber, double amount)
            throws Exception {

        AbstractAccount account =
                requireAbstractAccount(getRequiredAccount(accountNumber));

        TransactionCommand command = new DepositCommand(account, amount);
        command.execute();
        logger.log(command);

        return command.getTransaction();
    }

    public Transaction withdraw(
            int accountNumber,
            double amount,
            String pin) throws Exception {

        AbstractAccount account =
                requireAbstractAccount(getRequiredAccount(accountNumber));

        int numericPin;
        try {
            numericPin = Integer.parseInt(pin);
        } catch (NumberFormatException e) {
            throw new AccountException("Incorrect PIN");
        }

        if (pin == null || !pin.matches("\\d{4}")) {
            throw new AccountException("Incorrect PIN");
        }

        TransactionCommand command =
                new WithdrawCommand(account, amount, numericPin);

        command.execute();
        logger.log(command);

        return command.getTransaction();
    }

    public Transaction transfer(
            int fromAccountNumber,
            int toAccountNumber,
            double amount,
            String pin) throws Exception {

        IAccount from = getRequiredAccount(fromAccountNumber);
        IAccount to = getRequiredAccount(toAccountNumber);

        TransactionCommand command =
                new TransferCommand(from, to, amount, pin);

        command.execute();
        logger.log(command);

        return command.getTransaction();
    }

    public IAccount getAccount(int accountNumber) {
        return accounts.get(accountNumber);
    }

    public List<IAccount> getAllAccounts() {
        return new ArrayList<>(accounts.values());
    }

    public List<TransactionCommand> getTransactionHistory() {
        return logger.readAll();
    }

    public int getNextAccountNumber() {
        return nextAccountNumber;
    }

    private IAccount getRequiredAccount(int accountNumber)
            throws AccountException {

        IAccount account = accounts.get(accountNumber);

        if (account == null) {
            throw new AccountException(
                    "Account not found: " + accountNumber);
        }

        return account;
    }

    private AbstractAccount requireAbstractAccount(IAccount account)
            throws AccountException {

        if (!(account instanceof AbstractAccount)) {
            throw new AccountException(
                    "Unsupported account implementation");
        }

        return (AbstractAccount) account;
    }
}
