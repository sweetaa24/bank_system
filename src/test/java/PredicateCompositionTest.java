
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PredicateCompositionTest {

    @Test
    void combinesAccountConditions() {
        Repository<AccountNumber, BankAccount> repository =
                new InMemoryRepository<>();

        BlockedAccountRegistry blockedAccounts =
                new BlockedAccountRegistry();

        TransferService transferService = new TransferService(
                new NoCommission(),
                message -> {},
                blockedAccounts,
                new TransactionJournal()
        );

        BankService bank = new BankService(
                repository, transferService
        );

        BankAccount richDebit = new DebitAccount(
                "1111111111", "Ivan", 150000
        );
        BankAccount poorDebit = new DebitAccount(
                "2222222222", "Anna", 5000
        );
        BankAccount richCredit = new CreditAccount(
                "3333333333", "Petr", 50000, 50000
        );
        BankAccount richSavings = new SavingsAccount(
                "4444444444", "Maria", 120000, 1000
        );

        bank.openAccount(richDebit);
        bank.openAccount(poorDebit);
        bank.openAccount(richCredit);
        bank.openAccount(richSavings);

        Predicate<BankAccount> rich =
                a -> a.getBalance() >= 100_000;
        Predicate<BankAccount> notBlocked =
                a -> !blockedAccounts.isBlocked(a.getId());
        Predicate<BankAccount> debit =
                DebitAccount.class::isInstance;

        assertEquals(
                2,
                bank.findAccounts(rich).size()
        );

        assertEquals(
                4,
                bank.findAccounts(notBlocked).size()
        );

        assertEquals(
                2,
                bank.findAccounts(debit).size()
        );

        assertEquals(
                1,
                bank.findAccounts(rich.and(notBlocked).and(debit)).size()
        );

        assertEquals(
                2,
                bank.findAccounts(debit.negate()).size()
        );

        assertEquals(
                0,
                bank.findAccounts(
                        rich.and(CreditAccount.class::isInstance)
                ).size()
        );
    }
}
