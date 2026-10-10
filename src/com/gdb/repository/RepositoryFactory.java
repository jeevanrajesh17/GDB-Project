
package com.gdb.repository;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public final class RepositoryFactory {

    private static final String CONFIG_FILE =
            "config/persistence.properties";

    private RepositoryFactory() {
    }

    private static String getPersistenceMode() {
        Properties properties = new Properties();

        try (InputStream input = openConfig()) {
            if (input == null) {
                throw new IllegalStateException(
                        "Missing configuration: " + CONFIG_FILE);
            }

            properties.load(input);

        } catch (IOException e) {
            throw new IllegalStateException(
                    "Unable to load persistence configuration", e);
        }

        return properties.getProperty(
                "persistence.mode", "memory").trim().toLowerCase();
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

    public static AccountRepository createAccountRepository() {
        String mode = getPersistenceMode();

        switch (mode) {
            case "memory":
                return new InMemoryAccountRepository();

            case "jdbc":
            case "file":
                throw new UnsupportedOperationException(
                        "Persistence mode '" + mode
                                + "' is not implemented yet.");

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
            case "file":
                throw new UnsupportedOperationException(
                        "Persistence mode '" + mode
                                + "' is not implemented yet.");

            default:
                throw new IllegalArgumentException(
                        "Unknown persistence mode: " + mode);
        }
    }
}
