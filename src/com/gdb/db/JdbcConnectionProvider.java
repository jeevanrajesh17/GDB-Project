
package com.gdb.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JdbcConnectionProvider implements ConnectionProvider {

    private final String url;

    public JdbcConnectionProvider(String url, String driverClass)
            throws ClassNotFoundException {

        if (url == null || url.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Database URL cannot be empty");
        }

        this.url = url;

        Class.forName(driverClass);
    }

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url);
    }

    @Override
    public void shutdown() throws SQLException {
        // SQLite connections are closed by the caller.
        // No persistent connection is held by this provider.
    }

    @Override
    public String getProviderName() {
        return "JDBC";
    }
}
