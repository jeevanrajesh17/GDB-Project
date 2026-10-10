
package com.gdb.domain;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AccountRulesPropertiesLoader {

    private final Properties properties = new Properties();

    public AccountRulesPropertiesLoader(String filePath) {
        try (InputStream input = openPropertiesFile(filePath)) {
            if (input == null) {
                throw new IllegalArgumentException(
                        "Properties file not found: " + filePath);
            }

            properties.load(input);
            System.out.println(
                    "[Config] Loaded rules from " + filePath);

        } catch (IOException e) {
            throw new IllegalStateException(
                    "Unable to load properties file: " + filePath, e);
        }
    }

    private InputStream openPropertiesFile(String filePath)
            throws IOException {

        InputStream input = getClass().getClassLoader()
                .getResourceAsStream(filePath);

        if (input != null) {
            return input;
        }

        try {
            return new FileInputStream(filePath);
        } catch (IOException e) {
            return null;
        }
    }

    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public double getDouble(String key, double defaultValue) {
        String value = properties.getProperty(key);

        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }

        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Invalid decimal value for property: " + key, e);
        }
    }
}
