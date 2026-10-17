import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BankServiceTest {
    private BankService bankService;

    @BeforeEach
    void setUp() {
        Repository<AccountNumber, BankAccount> repository =
                new InMemoryRepository<>();

        TransferService transferService = new TransferService(
                new NoCommission(),
                new ConsoleNotificationService(),
                new BlockedAccountRegistry(),
                new TransactionJournal()
        );

        bankService = new BankService(repository, transferService);
    }

    @Test
    void openAccountAndFindAccount() {
        BankAccount account = new DebitAccount(
                "0000000001", "Ivan", 1000
        );

        bankService.openAccount(account);

        assertEquals(account, bankService.findAccount(
                new AccountNumber("0000000001")
        ));
    }

    @Test
    void registeringSameIdReplacesAccount() {
        BankAccount first = new DebitAccount(
                "0000000001", "Ivan", 1000
        );
        BankAccount second = new DebitAccount(
                "0000000001", "Petr", 2000
        );

        bankService.openAccount(first);
        bankService.openAccount(second);

        assertEquals(second, bankService.findAccount(
                new AccountNumber("0000000001")
        ));
    }

    @Test
    void closeAccountRemovesIt() {
        AccountNumber number = new AccountNumber("0000000001");

        bankService.openAccount(new DebitAccount(
                number.value(), "Ivan", 1000
        ));

        assertTrue(bankService.closeAccount(number));
        assertNull(bankService.findAccount(number));
    }

    @Test
    void openAccountRejectsNull() {
        assertThrows(
                IllegalArgumentException.class,
                () -> bankService.openAccount(null)
        );
    }
}
