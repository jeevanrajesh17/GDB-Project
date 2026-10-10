
package com.gdb.tests;

import java.util.List;

import com.gdb.domain.IAccount;
import com.gdb.domain.Transaction;
import com.gdb.logging.FileLogDestination;
import com.gdb.logging.TransactionLogger;
import com.gdb.repository.AccountRepository;
import com.gdb.repository.InMemoryAccountRepository;
import com.gdb.repository.InMemoryTransactionRepository;
import com.gdb.repository.RepositoryFactory;
import com.gdb.repository.TransactionRepository;
import com.gdb.service.AccountService;

public class TestRepositoryInMemory {

    public static void main(String[] args) throws Exception {

        System.out.println("=== Activity 21: Repository Pattern Test ===");

        // 1. Test account repository CRUD
        AccountRepository accounts =
                new InMemoryAccountRepository();

        IAccount first = RepositoryFactory
                .createAccountRepository()
                .findById(1001);

        System.out.println("\n--- Account Repository ---");

        IAccount account1 = com.gdb.domain.AccountFactory.createAccount(
                "SAVINGS", "1001", "John Doe",
                25, 15000.0, "ACTIVE", "1234");

        IAccount account2 = com.gdb.domain.AccountFactory.createAccount(
                "SAVINGS", "1002", "Jane Smith",
                30, 10000.0, "ACTIVE", "1234");

        accounts.save(account1);
        accounts.save(account2);

        if (accounts.exists(1001)
                && accounts.findById(1001) != null) {
            System.out.println("[PASS] save(), exists(), findById()");
        }

        if (accounts.findAll().size() == 2) {
            System.out.println("[PASS] findAll()");
        }

        account1.deposit(500.0);
        accounts.update(account1);

        if (accounts.findById(1001).getBalance() == 15500.0) {
            System.out.println("[PASS] update()");
        }

        accounts.delete(1002);

        if (!accounts.exists(1002)) {
            System.out.println("[PASS] delete()");
        }

        System.out.println("Next account number: "
                + accounts.nextAccountNumber());

        // 2. Test transaction repository
        System.out.println("\n--- Transaction Repository ---");

        TransactionRepository transactions =
                new InMemoryTransactionRepository();

        Transaction transaction = new Transaction(
                Transaction.generateId(),
                java.time.LocalDateTime.now(),
                1001,
                com.gdb.domain.TransactionType.DEPOSIT,
                500.0,
                15500.0,
                "SUCCESS",
                "Test deposit",
                0,
                1001);

        transactions.save(transaction);

        if (transactions.findAll().size() == 1) {
            System.out.println("[PASS] Transaction save() and findAll()");
        }

        if (transactions.findByAccount(1001).size() == 1) {
            System.out.println("[PASS] findByAccount()");
        }

        transactions.clear();

        if (transactions.findAll().isEmpty()) {
            System.out.println("[PASS] clear()");
        }

        // 3. Test factory configuration
        System.out.println("\n--- Repository Factory ---");

        if (RepositoryFactory.createAccountRepository()
                instanceof com.gdb.repository.InMemoryAccountRepository) {
            System.out.println("[PASS] Account repository factory");
        }

        if (RepositoryFactory.createTransactionRepository()
                instanceof com.gdb.repository.InMemoryTransactionRepository) {
            System.out.println("[PASS] Transaction repository factory");
        }

        // 4. Test service integration
        System.out.println("\n--- Account Service Integration ---");

        TransactionLogger logger =
                new TransactionLogger(new FileLogDestination());

        logger.clear();

        AccountService service = new AccountService(logger);

        IAccount john = service.openAccount(
                "SAVINGS", "John Doe", 25, 15000.0);

        IAccount jane = service.openAccount(
                "SAVINGS", "Jane Smith", 30, 10000.0);

        service.deposit(1001, 5000.0);
        service.withdraw(1001, 2000.0, "1234");
        service.transfer(1001, 1002, 1000.0, "1234");

        System.out.println("John's balance: " + john.getBalance());
        System.out.println("Jane's balance: " + jane.getBalance());

        List<Transaction> history =
                service.getRepositoryTransactionHistory();

        System.out.println("Repository transaction count: "
                + history.size());

        if (history.size() == 3) {
            System.out.println("[PASS] Service transactions saved in repository");
        } else {
            System.out.println("[FAIL] Expected 3 repository transactions");
        }

        System.out.println("\n=== Test Completed ===");
    }
}
