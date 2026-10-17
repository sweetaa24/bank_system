import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TransactionIndexTest {

    @Test
    void transactionCanBeFoundByEqualAccountNumber() {
        TransactionIndex index = new TransactionIndex();

        AccountNumber stored =
                new AccountNumber("1111111111");
        AccountNumber lookup =
                new AccountNumber("1111111111");

        Transaction transaction = new Transaction(
                1L,
                TransactionType.TRANSFER,
                stored,
                500,
                TransactionStatus.SUCCESS
        );

        index.add(transaction);

        assertEquals(1, index.findByAccount(lookup).size());
        assertEquals(transaction, index.findByAccount(lookup).get(0));
    }

    @Test
    void returnedListDoesNotExposeInternalState() {
        TransactionIndex index = new TransactionIndex();
        AccountNumber number = new AccountNumber("1111111111");

        index.add(new Transaction(
                1L,
                TransactionType.TRANSFER,
                number,
                500,
                TransactionStatus.SUCCESS
        ));

        index.findByAccount(number).clear();

        assertEquals(1, index.findByAccount(number).size());
    }
}
