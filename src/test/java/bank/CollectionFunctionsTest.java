package bank;

import bank.application.AccountSummary;
import bank.domain.AccountNumber;
import bank.domain.BankAccount;
import bank.domain.CreditAccount;
import bank.domain.DebitAccount;
import bank.domain.SavingsAccount;

import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CollectionFunctionsTest {

    @Test
    void filtersAccountsByConditions() {
        BankAccount debit1 =
                new DebitAccount("1111111111", "Анна", 5000);
        BankAccount debit2 =
                new DebitAccount("2222222222", "Иван", 150000);
        BankAccount debit3 =
                new DebitAccount("3333333333", "Мария", 0);

        List<BankAccount> accounts =
                List.of(debit1, debit2, debit3);

        Predicate<BankAccount> positiveBalance =
                account -> account.getBalance() > 0;
        Predicate<BankAccount> rich =
                account -> account.getBalance() >= 100_000;
        Predicate<BankAccount> debitOnly =
                DebitAccount.class::isInstance;

        assertEquals(
                List.of(debit1, debit2),
                CollectionFunctions.filter(accounts, positiveBalance)
        );

        assertEquals(
                List.of(debit2),
                CollectionFunctions.filter(accounts, rich)
        );

        assertEquals(
                List.of(debit1, debit2, debit3),
                CollectionFunctions.filter(accounts, debitOnly)
        );
    }

    @Test
    void mapsAccountsToSummaries() {
        List<BankAccount> accounts = List.of(
                new DebitAccount("1111111111", "Ivan", 150000),
                new CreditAccount("2222222222", "Anna", 50000, 100000),
                new SavingsAccount("3333333333", "Petr", 75000, 1000)
        );

        Function<BankAccount, AccountSummary> toSummary =
                account -> new AccountSummary(
                        account.getId(),
                        account.getClass().getSimpleName(),
                        account.getBalance()
                );

        List<AccountSummary> summaries =
                CollectionFunctions.map(accounts, toSummary);

        assertEquals(3, summaries.size());

        assertEquals(
                new AccountSummary(
                        new AccountNumber("1111111111"),
                        "DebitAccount",
                        150000
                ),
                summaries.get(0)
        );

        assertEquals(
                new AccountSummary(
                        new AccountNumber("2222222222"),
                        "CreditAccount",
                        50000
                ),
                summaries.get(1)
        );

        assertEquals(
                new AccountSummary(
                        new AccountNumber("3333333333"),
                        "SavingsAccount",
                        75000
                ),
                summaries.get(2)
        );
    }

    @Test
    void mapsAccountsToLabels() {
        List<BankAccount> accounts = List.of(
                new DebitAccount("1111111111", "Ivan", 150000),
                new CreditAccount("2222222222", "Anna", 50000, 100000)
        );

        Function<BankAccount, String> label =
                account -> account.getId() + " : " + account.getBalance();

        List<String> labels =
                CollectionFunctions.map(accounts, label);

        assertEquals(
                List.of(
                        "AccountNumber[value=1111111111] : 150000.0",
                        "AccountNumber[value=2222222222] : 50000.0"
                ),
                labels
        );
    }
}
