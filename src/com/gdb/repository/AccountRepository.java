
package com.gdb.repository;

import java.util.List;
import com.gdb.domain.IAccount;

public interface AccountRepository {

    void save(IAccount account);

    IAccount findById(int accountNumber);

    List<IAccount> findAll();

    void update(IAccount account);

    void delete(int accountNumber);

    boolean exists(int accountNumber);

    int nextAccountNumber();
}
