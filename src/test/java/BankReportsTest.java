import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BankReportsTest {

    private final BankReports reports = new BankReports();

    @Test
    void emptyJournalReturnsEmptyMaps() {
        assertTrue(reports.countByAccount(List.of()).isEmpty());
        assertTrue(reports.totalAmountByAccount(List.of()).isEmpty());
    }

    @Test
    void countsAndSumsTransactionsByAccount() {
        AccountNumber first = new AccountNumber("1111111111");
        AccountNumber second = new AccountNumber("2222222222");

        List<Transaction> transactions = List.of(
                new Transaction(1L, TransactionType.TRANSFER, first,
                        100.0, TransactionStatus.SUCCESS),
                new Transaction(2L, TransactionType.TRANSFER, second,
                        200.0, TransactionStatus.SUCCESS),
                new Transaction(3L, TransactionType.TRANSFER, first,
                        50.0, TransactionStatus.SUCCESS)
        );

        Map<AccountNumber, Integer> counts =
                reports.countByAccount(transactions);
        Map<AccountNumber, Double> totals =
                reports.totalAmountByAccount(transactions);

        assertEquals(2, counts.size());
        assertEquals(2, counts.get(first));
        assertEquals(1, counts.get(second));

        assertEquals(150.0, totals.get(first));
        assertEquals(200.0, totals.get(second));
    }

    @Test
    void equalAccountNumbersAreAggregated() {
        AccountNumber first = new AccountNumber("1111111111");
        AccountNumber second = new AccountNumber("1111111111");

        assertNotSame(first, second);
        assertEquals(first, second);

        List<Transaction> transactions = List.of(
                new Transaction(1L, TransactionType.TRANSFER, first,
                        100.0, TransactionStatus.SUCCESS),
                new Transaction(2L, TransactionType.TRANSFER, second,
                        50.0, TransactionStatus.SUCCESS)
        );

        assertEquals(1, reports.countByAccount(transactions).size());
        assertEquals(2, reports.countByAccount(transactions).get(first));
        assertEquals(150.0,
                reports.totalAmountByAccount(transactions).get(first));
    }
}