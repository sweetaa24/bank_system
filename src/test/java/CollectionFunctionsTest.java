
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Predicate;

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
}
