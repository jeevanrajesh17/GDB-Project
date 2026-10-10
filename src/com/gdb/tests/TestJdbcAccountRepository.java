
package com.gdb.tests;

import com.gdb.db.ConnectionProvider;
import com.gdb.domain.IAccount;
import com.gdb.domain.AccountFactory;
import com.gdb.repository.AccountRepository;
import com.gdb.repository.RepositoryFactory;

public class TestJdbcAccountRepository {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {

        System.out.println("=== Activity 23: JDBC Account Repository Test ===");

        try {
            ConnectionProvider provider =
                    RepositoryFactory.createConnectionProvider();

            AccountRepository repository =
                    RepositoryFactory.createAccountRepository();

            int account1 = 987651;
            int account2 = 987652;
            int missingAccount = 987653;

            if (repository.exists(account1)) {
    repository.delete(account1);
}

if (repository.exists(account2)) {
    repository.delete(account2);
}

            IAccount first = AccountFactory.createAccount(
                    "SAVINGS", String.valueOf(account1),
                    "Test User One", 25, 1000.0, "ACTIVE", "1234");

            IAccount second = AccountFactory.createAccount(
                    "SAVINGS", String.valueOf(account2),
                    "Test User Two", 25, 500.0, "ACTIVE", "5678");

            // CRUD: Create
            repository.save(first);
            repository.save(second);
            check(repository.exists(account1), "Account saved");

            // CRUD: Read
            IAccount retrieved = repository.findById(account1);
            check(retrieved != null
                    && retrieved.getName().equals("Test User One"),
                    "Account retrieved");

            // CRUD: Update
            retrieved = AccountFactory.createAccount(
                    "SAVINGS", String.valueOf(account1),
                    "Updated User One", 25, 1200.0,
                    "ACTIVE", "1234");

            repository.update(retrieved);

            check(repository.findById(account1).getBalance() == 1200.0,
                    "Account updated");

            check(repository.findAll().size() >= 2,
                    "Accounts listed");

            // Transfer success
            repository.transfer(account1, account2, 200.0);

            check(repository.findById(account1).getBalance() == 1000.0
                    && repository.findById(account2).getBalance() == 700.0,
                    "Transfer completed atomically");

            // Rollback test: destination does not exist
            double balanceBefore =
                    repository.findById(account1).getBalance();

            boolean transferFailed = false;

            try {
                repository.transfer(account1, missingAccount, 100.0);
            } catch (RuntimeException e) {
                transferFailed = true;
            }

            check(transferFailed, "Invalid transfer rejected");

            check(repository.findById(account1).getBalance() == balanceBefore,
                    "Failed transfer rolled back");

            // Insufficient funds
            boolean insufficientFunds = false;

            try {
                repository.transfer(account1, account2, 999999.0);
            } catch (RuntimeException e) {
                insufficientFunds = true;
            }

            check(insufficientFunds, "Insufficient funds rejected");

            // CRUD: Delete
            repository.delete(account2);
            check(!repository.exists(account2), "Account deleted");

            repository.delete(account1);
            check(!repository.exists(account1), "Test account cleanup");

        } catch (Exception e) {
            failed++;
            System.out.println("[FAIL] Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println();
        System.out.println("Passed: " + passed);
        System.out.println("Failed: " + failed);
        System.out.println("=== JDBC Account Repository Test Completed ===");
    }

    private static void check(boolean condition, String description) {
        if (condition) {
            passed++;
            System.out.println("[PASS] " + description);
        } else {
            failed++;
            System.out.println("[FAIL] " + description);
        }
    }
}
