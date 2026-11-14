
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TransferApplicationServiceTest {
    private Repository<AccountNumber, BankAccount> repository;
    private TransferApplicationService transferApplicationService;
    private BlockedAccountRegistry blockedAccounts;

    @BeforeEach
    void setUp() {
        repository = new InMemoryRepository<>();
        blockedAccounts = new BlockedAccountRegistry();

        TransferService transferService = new TransferService(
                new NoCommission(),
                message -> {},
                blockedAccounts,
                new TransactionJournal()
        );

        transferApplicationService = new TransferApplicationService(
                repository,
                transferService
        );
    }

    @Test
    void transferSuccessfully() {
        BankAccount from = new DebitAccount(
                "1111111111", "Ivan", 10000
        );
        BankAccount to = new DebitAccount(
                "2222222222", "Petr", 2000
        );

        repository.save(from);
        repository.save(to);
        transferApplicationService.transfer(
                from.getId(), to.getId(), 500
        );

        assertEquals(9500, from.getBalance());
        assertEquals(2500, to.getBalance());
    }

    @Test
    void transferFailsWhenSenderDoesNotExist() {
        BankAccount to = new DebitAccount(
                "2222222222", "Petr", 2000
        );

        repository.save(to);

        assertThrows(
                IllegalArgumentException.class,
                () -> transferApplicationService.transfer(
                        new AccountNumber("1111111111"),
                        to.getId(),
                        500
                )
        );

        assertEquals(2000, to.getBalance());
    }

    @Test
    void transferFailsWhenRecipientDoesNotExist() {
        BankAccount from = new DebitAccount(
                "1111111111", "Ivan", 10000
        );

        repository.save(from);

        assertThrows(
                IllegalArgumentException.class,
                () -> transferApplicationService.transfer(
                        from.getId(),
                        new AccountNumber("2222222222"),
                        500
                )
        );

        assertEquals(10000, from.getBalance());
    }

    @Test
    void transferFailsWhenFundsAreInsufficient() {
        BankAccount from = new DebitAccount(
                "1111111111", "Ivan", 100
        );
        BankAccount to = new DebitAccount(
                "2222222222", "Petr", 2000
        );

        repository.save(from);
        repository.save(to);

        assertThrows(
                InsufficientFundsException.class,
                () -> transferApplicationService.transfer(
                        from.getId(), to.getId(), 500
                )
        );

        assertEquals(100, from.getBalance());
        assertEquals(2000, to.getBalance());
    }

    @Test
    void transferFailsWhenSenderIsBlocked() {
        BankAccount from = new DebitAccount(
                "1111111111", "Ivan", 10000
        );
        BankAccount to = new DebitAccount(
                "2222222222", "Petr", 2000
        );

        repository.save(from);
        repository.save(to);

        blockedAccounts.block(from.getId());

        assertThrows(
                IllegalStateException.class,
                () -> transferApplicationService.transfer(
                        from.getId(), to.getId(), 500
                )
        );

        assertEquals(10000, from.getBalance());
        assertEquals(2000, to.getBalance());
    }
}
