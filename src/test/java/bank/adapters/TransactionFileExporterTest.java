package bank.adapters;

import bank.domain.AccountNumber;
import bank.domain.Transaction;
import bank.domain.TransactionStatus;
import bank.domain.TransactionType;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TransactionFileExporterTest {

    @TempDir
    Path directory;

    @Test
    void createsFileAndMissingDirectories() throws IOException {
        Path path = directory
                .resolve("export")
                .resolve("transactions.csv");

        new TransactionFileExporter().export(path, List.of(
                new Transaction(
                        1L,
                        TransactionType.TRANSFER,
                        new AccountNumber("1111111111"),
                        new AccountNumber("2222222222"),
                        1000.0,
                        TransactionStatus.SUCCESS
                )
        ));

        assertTrue(Files.exists(path));

        assertEquals(
                List.of(
                        "id;from;to;amount;status",
                        "1;1111111111;2222222222;1000.0;SUCCESS"
                ),
                Files.readAllLines(path)
        );
    }
}
