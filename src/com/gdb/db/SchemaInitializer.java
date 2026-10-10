
package com.gdb.db;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SchemaInitializer {

    private final ConnectionProvider provider;

    public SchemaInitializer(ConnectionProvider provider) {
        if (provider == null) {
            throw new IllegalArgumentException(
                    "Connection provider cannot be null");
        }
        this.provider = provider;
    }

    public void initialize() throws SQLException, IOException {
        List<String> statements = loadStatements();

        try (Connection connection = provider.getConnection();
             Statement statement = connection.createStatement()) {

            for (String sql : statements) {
                statement.execute(sql);
            }
        }
    }

    private List<String> loadStatements() throws IOException {
        InputStream input = getClass()
                .getClassLoader()
                .getResourceAsStream("schema.sql");

        if (input == null) {
            throw new IOException(
                    "schema.sql not found on the classpath");
        }

        List<String> statements = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(input, StandardCharsets.UTF_8))) {

            StringBuilder current = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                String trimmed = line.trim();

                if (trimmed.isEmpty() || trimmed.startsWith("--")) {
                    continue;
                }

                current.append(trimmed).append(' ');

                if (trimmed.endsWith(";")) {
                    statements.add(
                            current.substring(0, current.length() - 1).trim());
                    current.setLength(0);
                }
            }

            if (!current.toString().trim().isEmpty()) {
                statements.add(current.toString().trim());
            }
        }

        return statements;
    }
}
