package bank.adapters;

import bank.domain.Transaction;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public class TransactionFileExporter {

    public static final String SEPARATOR = ";";

    public static final String HEADER = String.join(
            SEPARATOR, "id", "from", "to", "amount", "status"
    );

    public void export(
            Path path,
            List<Transaction> transactions)
            throws IOException {

        Path directory = path.getParent();

        if (directory != null) {
            Files.createDirectories(directory);
        }

        List<String> lines = Stream.concat(
                        Stream.of(HEADER),
                        transactions.stream()
                                .map(TransactionDto::fromDomain)
                                .map(this::toLine))
                .toList();

        Files.write(path, lines);
    }

    private String toLine(TransactionDto dto) {
        return String.join(
                SEPARATOR,
                String.valueOf(dto.id()),
                dto.from(),
                dto.to(),
                String.valueOf(dto.amount()),
                dto.status()
        );
    }
}
