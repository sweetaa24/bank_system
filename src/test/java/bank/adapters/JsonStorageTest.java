package bank.adapters;

import bank.domain.CreditAccount;
import bank.domain.DebitAccount;
import bank.domain.SavingsAccount;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonStorageTest {

    @TempDir
    Path directory;

    private final JsonStorage storage = new JsonStorage();

    @Test
    void roundTripReturnsEqualList() throws IOException {
        Path path = directory.resolve("accounts.json");

        List<AccountDto> source = List.of(
                AccountDto.fromDomain(new DebitAccount(
                        "1111111111", "Ivan", 150000
                ))
        );

        storage.saveAccounts(path, source);

        List<AccountDto> restored = storage.loadAccounts(path);

        assertEquals(source, restored);
    }

    @Test
    void roundTripKeepsEmptyList() throws IOException {
        Path path = directory.resolve("empty.json");

        List<AccountDto> source = List.of();

        storage.saveAccounts(path, source);

        assertEquals(source, storage.loadAccounts(path));
    }

    @Test
    void roundTripKeepsAccountsOfDifferentTypes() throws IOException {
        Path path = directory.resolve("mixed.json");

        List<AccountDto> source = List.of(
                AccountDto.fromDomain(new DebitAccount(
                        "1111111111", "Ivan", 150000
                )),
                AccountDto.fromDomain(new CreditAccount(
                        "2222222222", "Anna", 200000, 50000
                )),
                AccountDto.fromDomain(new SavingsAccount(
                        "3333333333", "Maria", 75000, 1000
                ))
        );

        storage.saveAccounts(path, source);

        List<AccountDto> restored = storage.loadAccounts(path);

        assertEquals(source, restored);

        assertEquals(
                List.of("DebitAccount", "CreditAccount", "SavingsAccount"),
                restored.stream().map(AccountDto::type).toList()
        );
    }

    @Test
    void loadRejectsInvalidJson() throws IOException {
        Path path = directory.resolve("invalid.json");

        Files.writeString(path, "{ this is not json");

        assertThrows(
                IOException.class,
                () -> storage.loadAccounts(path)
        );
    }

    @Test
    void loadRejectsMissingFile() {
        Path path = directory.resolve("missing.json");

        assertFalse(Files.exists(path));

        assertThrows(
                IOException.class,
                () -> storage.loadAccounts(path)
        );
    }
}
