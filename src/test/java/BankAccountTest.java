import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BankAccountTest {

    @Test
    void accountsWithSameNumberAreEqual() {
        BankAccount debit =
                new DebitAccount("001", "Ivan", 10000);

        BankAccount savings =
                new SavingsAccount("001", "Petr", 5000, 1000);

        assertEquals(debit, savings);
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
    void accountEqualsItself() {
        BankAccount account =
                new DebitAccount("001", "Ivan", 10000);

        assertEquals(account, account);
    }

    @Test
    void accountDoesNotEqualNull() {
        BankAccount account =
                new DebitAccount("001", "Ivan", 10000);

        assertNotEquals(account, null);
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
