
package com.gdb.repository;

import java.util.List;
import com.gdb.domain.Transaction;

public interface TransactionRepository {

    void save(Transaction transaction);

    List<Transaction> findByAccount(int accountNumber);

    List<Transaction> findAll();

    void clear();
}
