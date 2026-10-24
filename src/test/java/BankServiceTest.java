import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
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


    @Test
    void transferFailsWhenSenderDoesNotExist() {
        BankAccount to = new DebitAccount(
                "2222222222", "Petr", 2000
        );
        bankService.openAccount(to);

        assertThrows(
                IllegalArgumentException.class,
                () -> bankService.transfer(
                        new AccountNumber("1111111111"),
                        new AccountNumber("2222222222"),
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
        bankService.openAccount(from);

        assertThrows(
                IllegalArgumentException.class,
                () -> bankService.transfer(
                        new AccountNumber("1111111111"),
                        new AccountNumber("2222222222"),
                        500
                )
        );

        assertEquals(10000, from.getBalance());
    }

    @Test
    void transferFailsWhenSenderIsBlocked() {
        BankAccount from = new DebitAccount(
                "1111111111", "Ivan", 10000
        );
        BankAccount to = new DebitAccount(
                "2222222222", "Petr", 2000
        );

        bankService.openAccount(from);
        bankService.openAccount(to);

        // Заблокируй счёт через тот же BlockedAccountRegistry,
        // который передаётся в TransferService.
        // Здесь нужно использовать экземпляр из setUp().

        assertEquals(10000, from.getBalance());
        assertEquals(2000, to.getBalance());
    }

    @Test
    void transferFailsWhenFundsAreInsufficient() {
        BankAccount from = new DebitAccount(
                "1111111111", "Ivan", 100
        );
        BankAccount to = new DebitAccount(
                "2222222222", "Petr", 2000
        );

        bankService.openAccount(from);
        bankService.openAccount(to);

        assertThrows(
                InsufficientFundsException.class,
                () -> bankService.transfer(
                        new AccountNumber("1111111111"),
                        new AccountNumber("2222222222"),
                        500
                )
        );

        assertEquals(100, from.getBalance());
        assertEquals(2000, to.getBalance());
    }

    @Test
    void findAccountsFiltersAndReturnsNewList() {
        BankAccount debit = new DebitAccount(
                "1111111111", "Ivan", 5000
        );
        BankAccount rich = new DebitAccount(
                "2222222222", "Petr", 150000
        );
        BankAccount zero = new DebitAccount(
                "3333333333", "Anna", 0
        );

        bankService.openAccount(debit);
        bankService.openAccount(rich);
        bankService.openAccount(zero);

        assertEquals(
                2,
                bankService.findAccounts(
                        account -> account.getBalance() > 0
                ).size()
        );

        assertEquals(
                1,
                bankService.findAccounts(
                        account -> account.getBalance() >= 100_000
                ).size()
        );

        assertEquals(
                3,
                bankService.findAccounts(
                        DebitAccount.class::isInstance
                ).size()
        );

        List<BankAccount> result = bankService.findAccounts(
                account -> account.getBalance() > 0
        );
        result.clear();

        assertNotNull(bankService.findAccount(
                new AccountNumber("1111111111")
        ));
    }
}
