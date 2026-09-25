import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DebitAccountTest {

    @Test
    void initialBalanceIsSaved() {
        DebitAccount account =
                new DebitAccount("1", "Иван", 10000);

        assertEquals(10000, account.getBalance());
    }

    @Test
    void depositIncreasesBalance() {
        DebitAccount account =
                new DebitAccount("1", "Иван", 10000);

        account.deposit(3000);

        assertEquals(13000, account.getBalance());
    }

    @Test
    void zeroDepositDoesNotChangeBalance() {
        DebitAccount account =
                new DebitAccount("1", "Иван", 10000);

        account.deposit(0);

        assertEquals(10000, account.getBalance());
    }

    @Test
    void negativeDepositDoesNotChangeBalance() {
        DebitAccount account =
                new DebitAccount("1", "Иван", 10000);

        account.deposit(-500);

        assertEquals(10000, account.getBalance());
    }

    @Test
    void withdrawDecreasesBalance() {
        DebitAccount account =
                new DebitAccount("1", "Иван", 10000);

        boolean result = account.withdraw(3000);

        assertTrue(result);
        assertEquals(7000, account.getBalance());
    }

    @Test
    void cannotWithdrawMoreThanBalance() {
        DebitAccount account =
                new DebitAccount("1", "Иван", 10000);

        boolean result = account.withdraw(15000);

        assertFalse(result);
        assertEquals(10000, account.getBalance());
    }

    @Test
    void zeroWithdrawIsForbidden() {
        DebitAccount account =
                new DebitAccount("1", "Иван", 10000);

        boolean result = account.withdraw(0);

        assertFalse(result);
        assertEquals(10000, account.getBalance());
    }

    @Test
    void negativeWithdrawIsForbidden() {
        DebitAccount account =
                new DebitAccount("1", "Иван", 10000);

        boolean result = account.withdraw(-500);

        assertFalse(result);
        assertEquals(10000, account.getBalance());
    }
}