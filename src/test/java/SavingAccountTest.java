import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SavingAccountTest {

    @Test
    void canWithdrawIfMinimumBalanceRemains() {
        SavingsAccount account =
                new SavingsAccount("0000000001", "Иван", 10000, 1000);

        account.withdraw(9000);

        assertEquals(1000, account.getBalance());
    }

    @Test
    void cannotWithdrawBelowMinimumBalance() {
        SavingsAccount account =
                new SavingsAccount("0000000001", "Иван", 10000, 1000);

        assertThrows(
                InsufficientFundsException.class,
                () -> account.withdraw(9500)
        );

        assertEquals(10000, account.getBalance());
    }

    @Test
    void balanceDoesNotChangeAfterFailedWithdraw() {
        SavingsAccount account =
                new SavingsAccount("0000000001", "Иван", 10000, 1000);

        assertThrows(
                InsufficientFundsException.class,
                () -> account.withdraw(9500)
        );

        assertEquals(10000, account.getBalance());
    }

    @Test
    void zeroWithdrawIsRejected() {
        SavingsAccount account =
                new SavingsAccount("0000000001", "Иван", 10000, 1000);

        assertThrows(
                IllegalArgumentException.class,
                () -> account.withdraw(0)
        );
    }

    @Test
    void negativeWithdrawIsRejected() {
        SavingsAccount account =
                new SavingsAccount("0000000001", "Иван", 10000, 1000);

        assertThrows(
                IllegalArgumentException.class,
                () -> account.withdraw(-500)
        );
    }
}
