import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BankAccountTest {

    @Test
    void accountsWithSameNumberAreEqual() {
        BankAccount debit =
                new DebitAccount("001", "Ivan", 10000);

        BankAccount savings =
                new SavingsAccount("001", "Petr", 5000, 1000);

        BankAccount credit =
                new CreditAccount("001", "Anna", 0, 5000);

        assertEquals(debit, savings);
        assertEquals(savings, credit);
        assertEquals(debit, credit);
    }

    @Test
    void accountsWithDifferentNumbersAreNotEqual() {
        BankAccount first =
                new DebitAccount("001", "Ivan", 10000);

        BankAccount second =
                new DebitAccount("002", "Ivan", 10000);

        assertNotEquals(first, second);
    }

    @Test
    void balanceDoesNotAffectEquality() {
        BankAccount first =
                new DebitAccount("001", "Ivan", 10000);

        BankAccount second =
                new DebitAccount("001", "Ivan", 5000);

        assertEquals(first, second);
    }

    @Test
    void equalAccountsHaveSameHashCode() {
        BankAccount debit =
                new DebitAccount("001", "Ivan", 10000);

        BankAccount savings =
                new SavingsAccount("001", "Petr", 5000, 1000);

        assertEquals(debit.hashCode(), savings.hashCode());
    }
}
