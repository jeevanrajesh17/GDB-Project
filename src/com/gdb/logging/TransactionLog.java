
package com.gdb.logging;

import com.gdb.command.TransactionCommand;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class TransactionLog {

    private static final String LOG_FILE = "data/transactions.ser";

    public void log(TransactionCommand cmd) throws IOException {
        File file = new File(LOG_FILE);

        File parent = file.getParentFile();
        if (parent != null) {
            parent.mkdirs();
        }

        boolean append = file.exists() && file.length() > 0;

        try (FileOutputStream fileOutput =
                     new FileOutputStream(file, append);
             ObjectOutputStream output = append
                     ? new AppendableObjectOutputStream(fileOutput)
                     : new ObjectOutputStream(fileOutput)) {

            output.writeObject(cmd);
            output.flush();
        }
    }

    public List<TransactionCommand> readAll()
            throws IOException, ClassNotFoundException {

        List<TransactionCommand> commands = new ArrayList<>();
        File file = new File(LOG_FILE);

        if (!file.exists() || file.length() == 0) {
            return commands;
        }

        try (ObjectInputStream input =
                     new ObjectInputStream(new FileInputStream(file))) {

            while (true) {
                try {
                    Object object = input.readObject();

                    if (object instanceof TransactionCommand) {
                        commands.add((TransactionCommand) object);
                    } else {
                        throw new IOException(
                            "Unexpected object found in transaction log"
                        );
                    }

                } catch (EOFException e) {
                    break;
                }
            }
        }

        return commands;
    }

    public void clear() throws IOException {
        File file = new File(LOG_FILE);

        if (file.exists() && !file.delete()) {
            throw new IOException(
                "Unable to delete transaction log: " + LOG_FILE
            );
        }
    }

    private static class AppendableObjectOutputStream
            extends ObjectOutputStream {

        AppendableObjectOutputStream(OutputStream output)
                throws IOException {
            super(output);
        }

        @Override
        protected void writeStreamHeader() throws IOException {
            reset();
        }
    }
}
