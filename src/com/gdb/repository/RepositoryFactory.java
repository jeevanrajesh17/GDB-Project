
package com.gdb.repository;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Properties;

import com.gdb.db.ConnectionProvider;
import com.gdb.db.JdbcConnectionProvider;
import com.gdb.db.SchemaInitializer;

public final class RepositoryFactory {

    private static final String CONFIG_FILE =
            "config/persistence.properties";

    private static Properties configuration;

    private RepositoryFactory() {
    }

    private static synchronized Properties getConfiguration() {
        if (configuration != null) {
            return configuration;
        }

        Properties properties = new Properties();

        try (InputStream input = openConfig()) {
            if (input == null) {
                throw new IllegalStateException(
                        "Missing configuration: " + CONFIG_FILE);
            }

            properties.load(input);
            configuration = properties;
            return configuration;

        } catch (IOException e) {
            throw new IllegalStateException(
                    "Unable to load persistence configuration", e);
        }
    }

    private static InputStream openConfig() throws IOException {
        InputStream input = RepositoryFactory.class
                .getClassLoader()
                .getResourceAsStream(CONFIG_FILE);

        if (input != null) {
            return input;
        }

        Path path = Paths.get(
                "src/main/resources", CONFIG_FILE);

        if (Files.exists(path)) {
            return Files.newInputStream(path);
        }

        return null;
    }

    private static String getPersistenceMode() {
        return getConfiguration()
                .getProperty("persistence.mode", "memory")
                .trim()
                .toLowerCase();
    }

    public static ConnectionProvider createConnectionProvider() {
        Properties properties = getConfiguration();
        String mode = getPersistenceMode();

        if (!"jdbc".equals(mode)) {
            throw new IllegalStateException(
                    "JDBC connection provider requested for mode: "
                            + mode);
        }

        String url = properties.getProperty("persistence.db.url");
        String driver = properties.getProperty(
                "persistence.db.driver", "org.sqlite.JDBC");

        try {
            JdbcConnectionProvider provider =
                    new JdbcConnectionProvider(url, driver);

            new SchemaInitializer(provider).initialize();

            return provider;

        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(
                    "JDBC driver not found: " + driver, e);

        } catch (SQLException | IOException e) {
            throw new IllegalStateException(
                    "Unable to initialize database schema", e);
        }
    }

    public static AccountRepository createAccountRepository() {
        String mode = getPersistenceMode();

        switch (mode) {
            case "memory":
                return new InMemoryAccountRepository();

            case "jdbc":
                createConnectionProvider();
                return new InMemoryAccountRepository();

            case "file":
                throw new UnsupportedOperationException(
                        "File persistence is not implemented yet.");

            default:
                throw new IllegalArgumentException(
                        "Unknown persistence mode: " + mode);
        }
    }

    public static TransactionRepository
            createTransactionRepository() {

        String mode = getPersistenceMode();

        switch (mode) {
            case "memory":
                return new InMemoryTransactionRepository();

            case "jdbc":
                createConnectionProvider();
                return new InMemoryTransactionRepository();

            case "file":
                throw new UnsupportedOperationException(
                        "File persistence is not implemented yet.");

            default:
                throw new IllegalArgumentException(
                        "Unknown persistence mode: " + mode);
        }
    }
}
