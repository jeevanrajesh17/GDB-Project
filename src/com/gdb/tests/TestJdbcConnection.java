
package com.gdb.tests;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import com.gdb.db.ConnectionProvider;
import com.gdb.db.SchemaInitializer;
import com.gdb.repository.RepositoryFactory;

public class TestJdbcConnection {

    public static void main(String[] args) {

        System.out.println("=== Activity 22: JDBC Connection Test ===");

        ConnectionProvider provider = null;

        try {
            // 1. Create the configured JDBC provider.
            provider = RepositoryFactory.createConnectionProvider();

            System.out.println("[PASS] Provider created: "
                    + provider.getProviderName());

            // 2. Test connection lifecycle.
            try (Connection connection = provider.getConnection()) {
                System.out.println("[PASS] Database connection established");

                try (Statement statement = connection.createStatement();
                     ResultSet result = statement.executeQuery("SELECT 1")) {

                    if (result.next() && result.getInt(1) == 1) {
                        System.out.println("[PASS] SELECT 1");
                    } else {
                        System.out.println("[FAIL] SELECT 1");
                    }
                }

                // 3. Verify required tables.
                try (ResultSet tables = connection.getMetaData()
                        .getTables(null, null, "accounts", null)) {

                    if (tables.next()) {
                        System.out.println("[PASS] accounts table exists");
                    } else {
                        System.out.println("[FAIL] accounts table missing");
                    }
                }

                try (ResultSet tables = connection.getMetaData()
                        .getTables(null, null, "transactions", null)) {

                    if (tables.next()) {
                        System.out.println("[PASS] transactions table exists");
                    } else {
                        System.out.println("[FAIL] transactions table missing");
                    }
                }
            }

            System.out.println("[PASS] Connection closed successfully");

            // 4. Verify schema initialization is idempotent.
            new SchemaInitializer(provider).initialize();
            System.out.println("[PASS] Schema initialization is repeatable");

            System.out.println("\n=== JDBC Test Completed ===");

        } catch (Exception e) {
            System.out.println("[FAIL] JDBC test failed: " + e.getMessage());
            e.printStackTrace();

        } finally {
            if (provider != null) {
                try {
                    provider.shutdown();
                } catch (Exception e) {
                    System.out.println(
                            "Provider shutdown warning: " + e.getMessage());
                }
            }
        }
    }
}
