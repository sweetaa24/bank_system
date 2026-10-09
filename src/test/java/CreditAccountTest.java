import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CreditAccountTest {

    @Test
    void accountCanHaveNegativeBalance() {
        CreditAccount account =
                new CreditAccount("0000000001", "Иван", 1000, 5000);

        account.withdraw(2000);
        assertEquals(-1000, account.getBalance());
    }

    @Test
    void canUseCreditLimit() {
        CreditAccount account =
                new CreditAccount("0000000001", "Иван", 1000, 5000);

        account.withdraw(6000);
        assertEquals(-5000, account.getBalance());
    }

    @Test
    void cannotExceedCreditLimit() {
        CreditAccount account =
                new CreditAccount("0000000001", "Иван", 1000, 5000);

        assertThrows(InsufficientFundsException.class, () -> account.withdraw(6001));

        assertEquals(1000, account.getBalance());
    }

    @Test
    void balanceDoesNotChangeAfterFailedWithdraw() {
        CreditAccount account =
                new CreditAccount("0000000001", "Иван", 1000, 5000);

        assertThrows(InsufficientFundsException.class, () -> account.withdraw(6001));

        assertEquals(1000, account.getBalance());
    }

    @Test
    void zeroWithdrawIsRejected() {
        CreditAccount account = new CreditAccount("0000000001", "Иван", 1000, 5000);

        assertThrows(IllegalArgumentException.class, () -> account.withdraw(0));
    }

    @Test
    void negativeWithdrawIsRejected() {
        CreditAccount account = new CreditAccount("0000000001", "Иван", 1000, 5000);
        assertThrows(IllegalArgumentException.class, () -> account.withdraw(-500));

    }
}