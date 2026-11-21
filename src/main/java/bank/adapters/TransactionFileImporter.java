package bank.adapters;

import bank.domain.TransactionStatus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class TransactionFileImporter {

    private static final int FIELD_COUNT = 5;

    public List<TransactionDto> load(Path path) throws IOException {
        List<String> lines = Files.readAllLines(path);
        List<TransactionDto> result = new ArrayList<>();

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);

            if (line.isBlank()) {
                continue;
            }

            if (i == 0 && line.equals(TransactionFileExporter.HEADER)) {
                continue;
            }

            result.add(parse(line, i + 1));
        }

        return result;
    }

    public long countSuccessful(Path path)
            throws IOException {
        try (Stream<String> lines =
                     Files.lines(path)) {
            return lines
                    .filter(line ->
                            line.contains("SUCCESS"))
                    .count();
        }
    }

    private TransactionDto parse(String line, int lineNumber) {
        String[] fields = line.split(
                TransactionFileExporter.SEPARATOR, -1
        );

        if (fields.length != FIELD_COUNT) {
            throw new IllegalArgumentException(
                    "Line " + lineNumber + ": expected "
                            + FIELD_COUNT + " fields but found "
                            + fields.length + ": " + line
            );
        }

        try {
            TransactionStatus status =
                    TransactionStatus.valueOf(fields[4].trim());

            return new TransactionDto(
                    Long.parseLong(fields[0].trim()),
                    fields[1].trim(),
                    fields[2].trim(),
                    Double.parseDouble(fields[3].trim()),
                    status.name()
            );
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Line " + lineNumber + " is malformed: " + line, e
            );
        }
    }
}
