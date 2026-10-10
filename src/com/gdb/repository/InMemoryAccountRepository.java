
package com.gdb.repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.gdb.domain.IAccount;

public class InMemoryAccountRepository implements AccountRepository {

    private final Map<Integer, IAccount> accounts =
            new LinkedHashMap<>();

    private int nextId = 1001;

    @Override
    public void save(IAccount account) {
        if (account == null) {
            throw new IllegalArgumentException(
                    "Account cannot be null");
        }

        int id = Integer.parseInt(account.getAccountNumber());

        if (accounts.containsKey(id)) {
            throw new IllegalArgumentException(
                    "Account already exists: " + id);
        }

        accounts.put(id, account);

        if (id >= nextId) {
            nextId = id + 1;
        }
    }

    @Override
    public IAccount findById(int accountNumber) {
        return accounts.get(accountNumber);
    }

    @Override
    public List<IAccount> findAll() {
        return new ArrayList<>(accounts.values());
    }

    @Override
    public void update(IAccount account) {
        if (account == null) {
            throw new IllegalArgumentException(
                    "Account cannot be null");
        }

        int id = Integer.parseInt(account.getAccountNumber());

        if (!accounts.containsKey(id)) {
            throw new IllegalArgumentException(
                    "Account not found: " + id);
        }

        accounts.put(id, account);
    }

    @Override
    public void delete(int accountNumber) {
        accounts.remove(accountNumber);
    }

    @Override
    public boolean exists(int accountNumber) {
        return accounts.containsKey(accountNumber);
    }

    @Override
    public int nextAccountNumber() {
        return nextId++;
    }

    @Override
    public void transfer(int fromAccount, int toAccount, double amount) {
        if (fromAccount == toAccount) {
            throw new IllegalArgumentException(
                    "Source and destination accounts must differ");
        }

        if (!Double.isFinite(amount) || amount <= 0) {
            throw new IllegalArgumentException(
                    "Transfer amount must be positive and finite");
        }

        IAccount source = accounts.get(fromAccount);
        IAccount destination = accounts.get(toAccount);

        if (source == null || destination == null) {
            throw new IllegalArgumentException(
                    "Source or destination account not found");
        }

        if (source.getBalance() < amount) {
            throw new IllegalArgumentException(
                    "Insufficient balance");
        }

        
        if (source instanceof com.gdb.domain.AbstractAccount
                && destination instanceof com.gdb.domain.AbstractAccount) {

            com.gdb.domain.AbstractAccount sourceAccount =
                    (com.gdb.domain.AbstractAccount) source;

            com.gdb.domain.AbstractAccount destinationAccount =
                    (com.gdb.domain.AbstractAccount) destination;

            double sourceBalance = source.getBalance();
            double destinationBalance = destination.getBalance();

            sourceAccount.setBalance(sourceBalance - amount);
            destinationAccount.setBalance(destinationBalance + amount);

        } else {
            throw new IllegalArgumentException(
                    "Unsupported account implementation");
        }

    }

}
