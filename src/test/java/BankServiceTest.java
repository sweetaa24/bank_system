import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Comparator;

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

    @Test
    void sortsAccountsByDifferentComparators() {
        Repository<AccountNumber, BankAccount> repository =
                new InMemoryRepository<>();

        BankService bank = new BankService(
                repository,
                new TransferService(
                        new NoCommission(),
                        message -> {},
                        new BlockedAccountRegistry(),
                        new TransactionJournal()
                )
        );

        BankAccount debit1 =
                new DebitAccount("1111111111", "Ivan", 150000);
        BankAccount debit2 =
                new DebitAccount("2222222222", "Anna", 50000);
        BankAccount credit =
                new CreditAccount("3333333333", "Petr", 100000, 50000);
        BankAccount savings =
                new SavingsAccount("4444444444", "Maria", 75000, 1000);

        bank.openAccount(debit1);
        bank.openAccount(debit2);
        bank.openAccount(credit);
        bank.openAccount(savings);

        Comparator<BankAccount> byBalance =
                Comparator.comparingDouble(BankAccount::getBalance);

        Comparator<BankAccount> byBalanceDesc =
                byBalance.reversed();

        Comparator<BankAccount> byNumber =
                Comparator.comparing(a -> a.getId().value());

        Comparator<BankAccount> byTypeThenBalance =
                Comparator
                        .comparing((BankAccount a) -> a.getClass().getSimpleName())
                        .thenComparingDouble(BankAccount::getBalance);

        assertEquals(
                java.util.List.of(debit2, savings, credit, debit1),
                bank.sortedAccounts(byBalance)
        );

        assertEquals(
                java.util.List.of(debit1, credit, savings, debit2),
                bank.sortedAccounts(byBalanceDesc)
        );

        assertEquals(
                java.util.List.of(debit1, debit2, credit, savings),
                bank.sortedAccounts(byNumber)
        );

        assertEquals(
                java.util.List.of(credit, debit2, debit1, savings),
                bank.sortedAccounts(byTypeThenBalance)
        );

        List<BankAccount> original = bank.findAccounts(a -> true);

        bank.sortedAccounts(byBalance);
        bank.sortedAccounts(byBalanceDesc);
        bank.sortedAccounts(byNumber);
        bank.sortedAccounts(byTypeThenBalance);

        assertEquals(original, bank.findAccounts(a -> true));
    }

    @Test
    void filtersAccountsUsingStream() {
        Repository<AccountNumber, BankAccount> repository =
                new InMemoryRepository<>();

        BankService bank = new BankService(
                repository,
                new TransferService(
                        new NoCommission(),
                        message -> {},
                        new BlockedAccountRegistry(),
                        new TransactionJournal()
                )
        );

        BankAccount debit1 =
                new DebitAccount("1111111111", "Ivan", 150000);
        BankAccount debit2 =
                new DebitAccount("2222222222", "Anna", 50000);
        BankAccount credit =
                new CreditAccount("3333333333", "Petr", 200000, 50000);

        bank.openAccount(debit1);
        bank.openAccount(debit2);
        bank.openAccount(credit);

        List<BankAccount> original = bank.findAccounts(a -> true);

        assertEquals(
                original.stream()
                        .filter(DebitAccount.class::isInstance)
                        .toList(),
                bank.findDebitAccounts()
        );

        assertEquals(
                List.of(debit1, credit),
                bank.findRichAccounts(100000)
        );


        assertEquals(
                List.of(debit2),
                bank.findAccountsByOwner("Anna")
        );

        assertEquals(original, bank.findAccounts(a -> true));
    }

    @Test
    void returnsEmptyListsWhenRepositoryIsEmpty() {
        assertTrue(bankService.findRichAccounts(100000).isEmpty());
        assertTrue(bankService.findDebitAccounts().isEmpty());
        assertTrue(bankService.findAccountsByOwner("Anna").isEmpty());
    }

    @Test
    void returnsAccountSummaries() {
        BankAccount debit = new DebitAccount(
                "1111111111", "Ivan", 150000
        );
        BankAccount credit = new CreditAccount(
                "2222222222", "Anna", 200000, 50000
        );

        bankService.openAccount(debit);
        bankService.openAccount(credit);

        List<AccountSummary> summaries = bankService.getAccountSummaries();

        assertEquals(2, summaries.size());

        assertTrue(summaries.contains(
                new AccountSummary(
                        debit.getId(),
                        "DebitAccount",
                        150000
                )
        ));

        assertTrue(summaries.contains(
                new AccountSummary(
                        credit.getId(),
                        "CreditAccount",
                        200000
                )
        ));
    }

    @Test
    void sortsAccountsWithoutChangingRepositoryOrder() {
        Repository<AccountNumber, BankAccount> repository =
                new InMemoryRepository<>();

        BankService bank = new BankService(
                repository,
                new TransferService(
                        new NoCommission(),
                        message -> {},
                        new BlockedAccountRegistry(),
                        new TransactionJournal()
                )
        );

        BankAccount debit1 = new DebitAccount(
                "1111111111", "Ivan", 150000
        );
        BankAccount debit2 = new DebitAccount(
                "4444444444", "Ivan", 50000
        );
        BankAccount credit = new CreditAccount(
                "3333333333", "Anna", 200000, 50000
        );
        BankAccount savings = new SavingsAccount(
                "2222222222", "Maria", 75000, 1000
        );

        bank.openAccount(debit1);
        bank.openAccount(debit2);
        bank.openAccount(credit);
        bank.openAccount(savings);

        List<BankAccount> original = repository.findAll();

        assertEquals(
                List.of(credit, debit1, savings, debit2),
                bank.richestFirst()
        );

        assertEquals(
                List.of(debit1, savings, credit, debit2),
                bank.accountsByNumber()
        );

        assertEquals(
                List.of(credit, debit2, debit1, savings),
                bank.accountsByTypeThenBalance()
        );

        assertEquals(
                List.of(credit, debit1, debit2, savings),
                bank.accountsByOwnerThenNumber()
        );

        assertEquals(original, repository.findAll());
    }

    @Test
    void checksAccountConditions() {
        Repository<AccountNumber, BankAccount> repository =
                new InMemoryRepository<>();

        BlockedAccountRegistry blockedAccounts = new BlockedAccountRegistry();

        BankService bank = new BankService(
                repository,
                new TransferService(
                        new NoCommission(),
                        message -> {},
                        blockedAccounts,
                        new TransactionJournal()
                )
        );

        BankAccount debit = new DebitAccount(
                "1111111111", "Ivan", 150000
        );

        BankAccount credit = new CreditAccount(
                "2222222222", "Anna", 200000, 50000
        );

        BankAccount savings = new SavingsAccount(
                "3333333333", "Maria", 75000, 1000
        );

        bank.openAccount(debit);
        bank.openAccount(credit);
        bank.openAccount(savings);

        blockedAccounts.block(debit.getId());

        assertTrue(bank.hasBlockedAccounts());
        assertTrue(bank.allAccountsPositive());
        assertFalse(bank.noCreditAccounts());
    }

    @Test
    void findsFirstRichAccountAndHandlesEmptyOptional() {
        Repository<AccountNumber, BankAccount> repository =
                new InMemoryRepository<>();

        BankService bank = new BankService(
                repository,
                new TransferService(
                        new NoCommission(),
                        message -> {},
                        new BlockedAccountRegistry(),
                        new TransactionJournal()
                )
        );

        BankAccount debit = new DebitAccount(
                "1111111111", "Ivan", 150000
        );

        BankAccount savings = new SavingsAccount(
                "2222222222", "Maria", 75000, 1000
        );

        bank.openAccount(debit);
        bank.openAccount(savings);

        BankAccount account = bank.findFirstRichAccount(100_000)
                .orElseThrow(() ->
                        new IllegalStateException("No rich account"));

        assertEquals(debit, account);

        String label = bank.findFirstRichAccount(100_000)
                .map(a -> a.getId().value())
                .orElse("not found");

        assertEquals("1111111111", label);

        String notFound = bank.findFirstRichAccount(300_000)
                .map(a -> a.getId().value())
                .orElse("not found");

        assertEquals("not found", notFound);
    }

    @Test
    void calculatesBalanceStatistics() {
        Repository<AccountNumber, BankAccount> repository =
                new InMemoryRepository<>();

        BankService bank = new BankService(
                repository,
                new TransferService(
                        new NoCommission(),
                        message -> {},
                        new BlockedAccountRegistry(),
                        new TransactionJournal()
                )
        );

        BankAccount debit = new DebitAccount(
                "1111111111", "Ivan", 150000
        );

        BankAccount credit = new CreditAccount(
                "2222222222", "Anna", 200000, 50000
        );

        BankAccount savings = new SavingsAccount(
                "3333333333", "Maria", 75000, 1000
        );

        bank.openAccount(debit);
        bank.openAccount(credit);
        bank.openAccount(savings);

        assertEquals(425000, bank.totalBalance());

        assertEquals(
                425000.0 / 3,
                bank.averageBalance().orElseThrow(),
                0.001
        );

        assertEquals(
                credit,
                bank.richestAccount().orElseThrow()
        );

        Repository<AccountNumber, BankAccount> emptyRepository =
                new InMemoryRepository<>();

        BankService emptyBank = new BankService(
                emptyRepository,
                new TransferService(
                        new NoCommission(),
                        message -> {},
                        new BlockedAccountRegistry(),
                        new TransactionJournal()
                )
        );

        assertEquals(0, emptyBank.totalBalance());
        assertTrue(emptyBank.averageBalance().isEmpty());
        assertTrue(emptyBank.richestAccount().isEmpty());
    }


}
