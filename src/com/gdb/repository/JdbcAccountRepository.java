
package com.gdb.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.gdb.db.ConnectionProvider;
import com.gdb.domain.AbstractAccount;
import com.gdb.domain.AccountFactory;
import com.gdb.domain.IAccount;
import com.gdb.exceptions.DataAccessException;

public class JdbcAccountRepository implements AccountRepository {

    private final ConnectionProvider connectionProvider;

    public JdbcAccountRepository(ConnectionProvider connectionProvider) {
        if (connectionProvider == null) {
            throw new IllegalArgumentException(
                "Connection provider cannot be null");
        }

        this.connectionProvider = connectionProvider;
        migrateSchema();
    }

    private void migrateSchema() {
        try (Connection connection = connectionProvider.getConnection();
             Statement statement = connection.createStatement();
             ResultSet columns = statement.executeQuery(
                 "PRAGMA table_info(accounts)")) {

            boolean pinExists = false;

            while (columns.next()) {
                if ("pin".equalsIgnoreCase(columns.getString("name"))) {
                    pinExists = true;
                    break;
                }
            }

            if (!pinExists) {
                statement.executeUpdate(
                    "ALTER TABLE accounts ADD COLUMN pin " +
                    "TEXT NOT NULL DEFAULT '1234'");
            }

        } catch (SQLException e) {
            throw new DataAccessException(
                "Failed to migrate accounts schema", e);
        }
    }

    @Override
    public void save(IAccount account) {
        if (account == null) {
            throw new IllegalArgumentException("Account cannot be null");
        }

        if (!(account instanceof AbstractAccount)) {
            throw new IllegalArgumentException(
                "Account must extend AbstractAccount");
        }

        AbstractAccount details = (AbstractAccount) account;

        String sql = "INSERT INTO accounts " +
            "(account_number, name, age, balance, account_type, status, pin) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, Integer.parseInt(account.getAccountNumber()));
            statement.setString(2, account.getName());
            statement.setInt(3, details.getAge());
            statement.setDouble(4, account.getBalance());
            statement.setString(5, account.getAccountType());
            statement.setString(6, account.getStatus());
            statement.setString(7, details.getPin());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DataAccessException("Failed to save account", e);
        }
    }

    @Override
    public IAccount findById(int accountNumber) {
        String sql = "SELECT * FROM accounts WHERE account_number = ?";

        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, accountNumber);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return mapAccount(result);
                }
                return null;
            }

        } catch (SQLException e) {
            throw new DataAccessException("Failed to find account", e);
        }
    }

    @Override
    public List<IAccount> findAll() {
        List<IAccount> accounts = new ArrayList<>();
        String sql = "SELECT * FROM accounts ORDER BY account_number";

        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                accounts.add(mapAccount(result));
            }

            return accounts;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to retrieve accounts", e);
        }
    }

    @Override
    public void update(IAccount account) {
        if (account == null) {
            throw new IllegalArgumentException("Account cannot be null");
        }

        if (!(account instanceof AbstractAccount)) {
            throw new IllegalArgumentException(
                "Account must extend AbstractAccount");
        }

        AbstractAccount details = (AbstractAccount) account;

        String sql = "UPDATE accounts SET name = ?, age = ?, balance = ?, " +
            "account_type = ?, status = ?, pin = ? WHERE account_number = ?";

        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, account.getName());
            statement.setInt(2, details.getAge());
            statement.setDouble(3, account.getBalance());
            statement.setString(4, account.getAccountType());
            statement.setString(5, account.getStatus());
            statement.setString(6, details.getPin());
            statement.setInt(7, Integer.parseInt(account.getAccountNumber()));

            if (statement.executeUpdate() == 0) {
                throw new DataAccessException("Account not found: " +
                    account.getAccountNumber());
            }

        } catch (SQLException e) {
            throw new DataAccessException("Failed to update account", e);
        }
    }

    @Override
    public void delete(int accountNumber) {
        String sql = "DELETE FROM accounts WHERE account_number = ?";

        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, accountNumber);

            if (statement.executeUpdate() == 0) {
                throw new DataAccessException(
                    "Account not found: " + accountNumber);
            }

        } catch (SQLException e) {
            throw new DataAccessException("Failed to delete account", e);
        }
    }

    @Override
    public boolean exists(int accountNumber) {
        String sql =
            "SELECT 1 FROM accounts WHERE account_number = ?";

        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, accountNumber);

            try (ResultSet result = statement.executeQuery()) {
                return result.next();
            }

        } catch (SQLException e) {
            throw new DataAccessException("Failed to check account", e);
        }
    }

    @Override
    public int nextAccountNumber() {
        String sql =
            "SELECT COALESCE(MAX(account_number), 1000) + 1 FROM accounts";

        try (Connection connection = connectionProvider.getConnection();
             Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery(sql)) {

            return result.next() ? result.getInt(1) : 1001;

        } catch (SQLException e) {
            throw new DataAccessException(
                "Failed to generate account number", e);
        }
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

        String debitSql =
            "UPDATE accounts SET balance = balance - ? " +
            "WHERE account_number = ? AND balance >= ?";

        String creditSql =
            "UPDATE accounts SET balance = balance + ? " +
            "WHERE account_number = ?";

        try (Connection connection = connectionProvider.getConnection()) {
            connection.setAutoCommit(false);

            try {
                try (PreparedStatement debit =
                         connection.prepareStatement(debitSql)) {

                    debit.setDouble(1, amount);
                    debit.setInt(2, fromAccount);
                    debit.setDouble(3, amount);

                    if (debit.executeUpdate() != 1) {
                        throw new DataAccessException(
                            "Source account not found or insufficient balance");
                    }
                }

                try (PreparedStatement credit =
                         connection.prepareStatement(creditSql)) {

                    credit.setDouble(1, amount);
                    credit.setInt(2, toAccount);

                    if (credit.executeUpdate() != 1) {
                        throw new DataAccessException(
                            "Destination account not found");
                    }
                }

                connection.commit();

            } catch (Exception e) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackError) {
                    e.addSuppressed(rollbackError);
                }

                if (e instanceof RuntimeException) {
                    throw (RuntimeException) e;
                }

                throw new DataAccessException(
                    "Transfer failed and was rolled back", e);

            } finally {
                try {
                    connection.setAutoCommit(true);
                } catch (SQLException ignored) {
                    // The connection is about to close.
                }
            }

        } catch (SQLException e) {
            throw new DataAccessException(
                "Database error during transfer", e);
        }
    }

    private IAccount mapAccount(ResultSet result) throws SQLException {
        return AccountFactory.createAccount(
            result.getString("account_type"),
            String.valueOf(result.getInt("account_number")),
            result.getString("name"),
            result.getInt("age"),
            result.getDouble("balance"),
            result.getString("status"),
            result.getString("pin")
        );
    }
}
