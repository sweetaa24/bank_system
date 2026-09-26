import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SavingAccountTest {

    @Test
    void canWithdrawIfMinimumBalanceRemains() {
        SavingsAccount account =
                new SavingsAccount("0000000001", "Иван", 10000, 1000);

        boolean result = account.withdraw(9000);

        assertTrue(result);
        assertEquals(1000, account.getBalance());
    }

    @Test
    void cannotWithdrawBelowMinimumBalance() {
        SavingsAccount account =
                new SavingsAccount("0000000001", "Иван", 10000, 1000);

        boolean result = account.withdraw(9500);

        assertFalse(result);
        assertEquals(10000, account.getBalance());
    }

    @Test
    void balanceDoesNotChangeAfterFailedWithdraw() {
        SavingsAccount account =
                new SavingsAccount("0000000001", "Иван", 10000, 1000);

        account.withdraw(9500);

        assertEquals(10000, account.getBalance());
    }
}