
package com.gdb.service;

import java.util.ArrayList;
import java.util.List;

import com.gdb.command.DepositCommand;
import com.gdb.command.TransactionCommand;
import com.gdb.command.TransferCommand;
import com.gdb.command.WithdrawCommand;
import com.gdb.domain.AbstractAccount;
import com.gdb.domain.IAccount;
import com.gdb.domain.Transaction;
import com.gdb.exceptions.AccountException;
import com.gdb.logging.TransactionLogger;
import com.gdb.repository.AccountRepository;
import com.gdb.repository.RepositoryFactory;
import com.gdb.repository.TransactionRepository;

public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionLogger logger;

    public AccountService(TransactionLogger logger) {
        if (logger == null) {
            throw new IllegalArgumentException(
                    "Transaction logger cannot be null");
        }

        this.logger = logger;
        this.accountRepository =
                RepositoryFactory.createAccountRepository();
        this.transactionRepository =
                RepositoryFactory.createTransactionRepository();
    }

    public IAccount openAccount(
            String type,
            String name,
            int age,
            double initialBalance) {

        int accountNumber =
                accountRepository.nextAccountNumber();

        IAccount account =
                com.gdb.domain.AccountFactory.createAccount(
                        type,
                        String.valueOf(accountNumber),
                        name,
                        age,
                        initialBalance,
                        "ACTIVE",
                        "1234");

        accountRepository.save(account);
        return account;
    }

    public void closeAccount(int accountNumber, String pin)
            throws AccountException {

        IAccount account = getRequiredAccount(accountNumber);
        AbstractAccount abstractAccount =
                requireAbstractAccount(account);

        if (!abstractAccount.validatePin(pin)) {
            throw new AccountException("Incorrect PIN");
        }

        if (!"ACTIVE".equals(account.getStatus())) {
            throw new AccountException("Account is not active");
        }

        abstractAccount.setStatus("CLOSED");
        accountRepository.update(account);
    }

    public Transaction deposit(int accountNumber, double amount)
            throws Exception {

        AbstractAccount account = requireAbstractAccount(
                getRequiredAccount(accountNumber));

        TransactionCommand command =
                new DepositCommand(account, amount);

        command.execute();

        Transaction transaction = command.getTransaction();
        transactionRepository.save(transaction);
        logger.log(command);

        accountRepository.update(account);
        return transaction;
    }

    public Transaction withdraw(
            int accountNumber,
            double amount,
            String pin) throws Exception {

        AbstractAccount account = requireAbstractAccount(
                getRequiredAccount(accountNumber));

        if (pin == null || !pin.matches("\\d{4}")) {
            throw new AccountException("Incorrect PIN");
        }

        int numericPin = Integer.parseInt(pin);

        TransactionCommand command =
                new WithdrawCommand(account, amount, numericPin);

        command.execute();

        Transaction transaction = command.getTransaction();
        transactionRepository.save(transaction);
        logger.log(command);

        accountRepository.update(account);
        return transaction;
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

        Transaction transaction = command.getTransaction();
        transactionRepository.save(transaction);
        logger.log(command);

        accountRepository.update(from);
        accountRepository.update(to);

        return transaction;
    }

    public IAccount getAccount(int accountNumber) {
        return accountRepository.findById(accountNumber);
    }

    public List<IAccount> getAllAccounts() {
        return accountRepository.findAll();
    }

    public List<TransactionCommand> getTransactionHistory() {
        return logger.readAll();
    }

    public List<Transaction> getRepositoryTransactionHistory() {
        return transactionRepository.findAll();
    }

    public List<Transaction> getTransactionsForAccount(
            int accountNumber) {

        return transactionRepository.findByAccount(accountNumber);
    }

    public int getNextAccountNumber() {
        return accountRepository.nextAccountNumber();
    }

    private IAccount getRequiredAccount(int accountNumber)
            throws AccountException {

        IAccount account =
                accountRepository.findById(accountNumber);

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
