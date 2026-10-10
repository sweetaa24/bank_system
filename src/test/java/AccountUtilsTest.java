import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AccountUtilsTest {
    @Test
    void copiesDebitAccountsToBankAccounts() {
        List<DebitAccount> source = List.of(
                new DebitAccount("1234567890", "Ivan", 1000),
                new DebitAccount("1234567891", "Anna", 2000)
        );

        List<BankAccount> target = new ArrayList<>();

        AccountUtils.copy(source, target);

        assertEquals(2, target.size());
        assertEquals(source.get(0), target.get(0));
        assertEquals(source.get(1), target.get(1));
    }

    @Test
    void copiesDebitAccountsToObjects() {
        List<DebitAccount> source = List.of(
                new DebitAccount("1234567890", "Ivan", 1000)
        );

        List<Object> target = new ArrayList<>();

        AccountUtils.copy(source, target);

        assertEquals(1, target.size());
        assertSame(source.get(0), target.get(0));
    }

    @Test
    void copiesBankAccountsToBankAccounts() {
        List<BankAccount> source = List.of(
                new DebitAccount("1234567890", "Ivan", 1000),
                new SavingsAccount("1234567891", "Anna", 2000, 500)
        );

        List<BankAccount> target = new ArrayList<>();

        AccountUtils.copy(source, target);

        assertEquals(2, target.size());
        assertEquals(source, target);
    }

    @Test
    void preservesElementOrder() {
        List<DebitAccount> source = List.of(
                new DebitAccount("1234567890", "Ivan", 1000),
                new DebitAccount("1234567891", "Anna", 2000),
                new DebitAccount("1234567892", "Petr", 3000)
        );

        List<BankAccount> target = new ArrayList<>();

        AccountUtils.copy(source, target);

        for (int i = 0; i < source.size(); i++) {
            assertSame(source.get(i), target.get(i));
        }
    }

    @Test
    void richestReturnsAccountWithMaximumBalance() {
        List<DebitAccount> accounts = List.of(
                new DebitAccount("1234567890", "Ivan", 1000),
                new DebitAccount("1234567891", "Anna", 5000),
                new DebitAccount("1234567892", "Petr", 3000)
        );

        DebitAccount result = AccountUtils.richest(accounts);

        assertEquals(5000, result.getBalance());
    }

    @Test
    void richestReturnsOnlyAccount() {
        DebitAccount account = new DebitAccount(
                "1234567890", "Ivan", 1000
        );

        DebitAccount result = AccountUtils.richest(
                List.of(account)
        );

        assertSame(account, result);
    }

    @Test
    void richestReturnsFirstAccountWhenBalancesAreEqual() {
        DebitAccount first = new DebitAccount(
                "1234567890", "Ivan", 5000
        );
        DebitAccount second = new DebitAccount(
                "1234567891", "Anna", 5000
        );

        DebitAccount result = AccountUtils.richest(
                List.of(first, second)
        );

        assertSame(first, result);
    }

    @Test
    void richestThrowsExceptionForEmptyList() {
        assertThrows(
                IllegalArgumentException.class,
                () -> AccountUtils.richest(List.<DebitAccount>of())
        );
    }
}
