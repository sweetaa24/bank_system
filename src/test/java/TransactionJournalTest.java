
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TransactionJournalTest {

    @Test
    void transactionsAreStoredInInsertionOrder() {
        TransactionJournal journal = new TransactionJournal();
        Transaction first = new Transaction(
                1L, TransactionType.TRANSFER,
                new AccountNumber("0000000001"),
                100, TransactionStatus.SUCCESS
        );
        Transaction second = new Transaction(
                2L, TransactionType.DEPOSIT,
                new AccountNumber("0000000002"),
                200, TransactionStatus.SUCCESS
        );

        journal.add(first);
        journal.add(second);

        assertEquals(first, journal.findAll().get(0));
        assertEquals(second, journal.findAll().get(1));
    }

    @Test
    void identicalTransactionsCanBeStoredTwice() {
        TransactionJournal journal = new TransactionJournal();
        Transaction transaction = new Transaction(
                1L, TransactionType.TRANSFER,
                new AccountNumber("0000000001"),
                100, TransactionStatus.SUCCESS
        );

        journal.add(transaction);
        journal.add(transaction);

        assertEquals(2, journal.size());
    }

    @Test
    void sizeMatchesNumberOfTransactions() {
        TransactionJournal journal = new TransactionJournal();

        journal.add(new Transaction(
                1L, TransactionType.TRANSFER,
                new AccountNumber("0000000001"),
                100, TransactionStatus.SUCCESS
        ));

        assertEquals(1, journal.size());
    }

    @Test
    void returnedListDoesNotExposeInternalState() {
        TransactionJournal journal = new TransactionJournal();
        Transaction transaction = new Transaction(
                1L, TransactionType.TRANSFER,
                new AccountNumber("0000000001"),
                100, TransactionStatus.SUCCESS
        );
        journal.add(transaction);

        List<Transaction> copy = journal.findAll();
        copy.clear();

        assertEquals(1, journal.size());
    }
}
