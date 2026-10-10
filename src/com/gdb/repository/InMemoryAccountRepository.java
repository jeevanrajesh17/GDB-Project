
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
}
