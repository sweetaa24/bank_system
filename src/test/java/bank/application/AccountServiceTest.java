package bank.application;

import bank.adapters.InMemoryAccountRepository;
import bank.domain.AccountNumber;
import bank.domain.BankAccount;
import bank.domain.DebitAccount;
import bank.ports.AccountRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class AccountServiceTest {
    private AccountService accountService;
    private AccountRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryAccountRepository();
        accountService = new AccountService(repository);
    }

    @Test
    void openAccountAndFindAccount() {
        BankAccount account = new DebitAccount(
                "0000000001", "Ivan", 1000
        );

        accountService.openAccount(account);

        assertEquals(
                Optional.of(account),
                accountService.findAccount(
                        new AccountNumber("0000000001")
                )
        );
    }

    @Test
    void findAccountReturnsEmptyWhenAccountDoesNotExist() {
        Optional<BankAccount> result = accountService.findAccount(
                new AccountNumber("0000000001")
        );

        assertTrue(result.isEmpty());
    }

    @Test
    void closeAccountRemovesIt() {
        AccountNumber number = new AccountNumber("0000000001");

        accountService.openAccount(new DebitAccount(
                number.value(), "Ivan", 1000
        ));

        assertTrue(accountService.closeAccount(number));
        assertTrue(accountService.findAccount(number).isEmpty());
    }

    @Test
    void closeAccountReturnsFalseWhenAccountDoesNotExist() {
        assertFalse(accountService.closeAccount(
                new AccountNumber("0000000001")
        ));
    }

    @Test
    void openAccountRejectsNull() {
        assertThrows(
                IllegalArgumentException.class,
                () -> accountService.openAccount(null)
        );
    }

    @Test
    void registeringSameIdReplacesAccount() {
        BankAccount first = new DebitAccount(
                "0000000001", "Ivan", 1000
        );

        BankAccount second = new DebitAccount(
                "0000000001", "Petr", 2000
        );

        accountService.openAccount(first);
        accountService.openAccount(second);

        assertEquals(
                Optional.of(second),
                accountService.findAccount(
                        new AccountNumber("0000000001")
                )
        );
    }
}
