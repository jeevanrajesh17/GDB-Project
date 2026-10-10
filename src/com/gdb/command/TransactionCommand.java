package com.gdb.command;

import java.io.Serializable;
import com.gdb.domain.Transaction;

public interface TransactionCommand extends Serializable {

    void execute() throws Exception;

    Transaction getTransaction();
}