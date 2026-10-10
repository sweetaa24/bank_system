import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionTest {

    @Test
    void transactionIsCreatedCorrectly() {
        AccountNumber account = new AccountNumber("1234567890");

        Transaction transaction = new Transaction(1L, TransactionType.DEPOSIT, account, 5000, TransactionStatus.SUCCESS);

        assertEquals(TransactionType.DEPOSIT, transaction.type());
        assertEquals(account, transaction.account());
        assertEquals(5000, transaction.amount());
        assertEquals(TransactionStatus.SUCCESS, transaction.status());
    }

    @Test
    void transactionCanBePrinted() {
        Transaction transaction = new Transaction(1L, TransactionType.DEPOSIT, new AccountNumber("1234567890"), 5000, TransactionStatus.SUCCESS);

        assertNotNull(transaction.toString());
    }
}