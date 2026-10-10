import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryRepositoryTest {
    @Test
    void saveAndFindById() {
        Repository<AccountNumber, BankAccount> repository =
                new InMemoryRepository<>();

        BankAccount account = new DebitAccount(
                "1234567890", "Ivan", 1000
        );

        repository.save(account);

        assertEquals(account, repository.findById(
                new AccountNumber("1234567890")
        ));
    }

    @Test
    void findByIdReturnsNullWhenNotFound() {
        Repository<AccountNumber, BankAccount> repository =
                new InMemoryRepository<>();

        assertNull(repository.findById(
                new AccountNumber("1234567890")
        ));
    }

    @Test
    void saveReplacesEntityWithSameId() {
        Repository<AccountNumber, BankAccount> repository =
                new InMemoryRepository<>();

        BankAccount first = new DebitAccount(
                "1234567890", "Ivan", 1000
        );

        BankAccount second = new DebitAccount(
                "1234567890", "Ivan", 5000
        );

        repository.save(first);
        repository.save(second);

        assertEquals(1, repository.size());
        assertEquals(5000, repository.findById(
                new AccountNumber("1234567890")
        ).getBalance());
    }

    @Test
    void saveNullThrowsException() {
        Repository<AccountNumber, BankAccount> repository =
                new InMemoryRepository<>();

        assertThrows(
                IllegalArgumentException.class,
                () -> repository.save(null)
        );
    }
}
