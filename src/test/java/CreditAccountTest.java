import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CreditAccountTest {

    @Test
    void accountCanHaveNegativeBalance() {
        CreditAccount account =
                new CreditAccount("0000000001", "Иван", 1000, 5000);

        boolean result = account.withdraw(2000);

        assertTrue(result);
        assertEquals(-1000, account.getBalance());
    }

    @Test
    void canUseCreditLimit() {
        CreditAccount account =
                new CreditAccount("0000000001", "Иван", 1000, 5000);

        boolean result = account.withdraw(6000);

        assertTrue(result);
        assertEquals(-5000, account.getBalance());
    }

    @Test
    void cannotExceedCreditLimit() {
        CreditAccount account =
                new CreditAccount("0000000001", "Иван", 1000, 5000);

        boolean result = account.withdraw(6001);

        assertFalse(result);
        assertEquals(1000, account.getBalance());
    }

    @Test
    void balanceDoesNotChangeAfterFailedWithdraw() {
        CreditAccount account =
                new CreditAccount("0000000001", "Иван", 1000, 5000);

        account.withdraw(6001);

        assertEquals(1000, account.getBalance());
    }
}