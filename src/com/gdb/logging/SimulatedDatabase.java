
package com.gdb.logging;

import com.gdb.command.TransactionCommand;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimulatedDatabase {

    private final Map<String, List<TransactionCommand>> tables =
            new HashMap<>();

    public void insert(String tableName, TransactionCommand cmd) {
        tables.computeIfAbsent(
            tableName, key -> new ArrayList<>()
        ).add(cmd);
    }

    public List<TransactionCommand> selectAll(String tableName) {
        return new ArrayList<>(
            tables.getOrDefault(tableName, new ArrayList<>())
        );
    }

    public void deleteAll(String tableName) {
        tables.remove(tableName);
    }
}
