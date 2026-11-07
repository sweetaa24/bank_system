import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BankReports {

    public Map<AccountNumber, Integer> countByAccount(
            List<Transaction> transactions) {

        return transactions.stream()
                .collect(Collectors.toMap(
                        Transaction::account,
                        tx -> 1,
                        Integer::sum
                ));
    }

    public Map<AccountNumber, Double> totalAmountByAccount(
            List<Transaction> transactions) {

        return transactions.stream()
                .collect(Collectors.toMap(
                        Transaction::account,
                        Transaction::amount,
                        Double::sum
                ));

    }
}