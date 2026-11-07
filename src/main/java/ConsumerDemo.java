import java.util.List;
import java.util.function.Consumer;

public class ConsumerDemo {

    public static void main(String[] args) {
        List<AccountSummary> summaries = List.of(
                new AccountSummary(
                        new AccountNumber("1111111111"),
                        "DebitAccount",
                        150000
                ),
                new AccountSummary(
                        new AccountNumber("2222222222"),
                        "CreditAccount",
                        50000
                )
        );

        Consumer<AccountSummary> printer =
                summary -> System.out.println(summary);

        CollectionFunctions.forEach(
                summaries,
                printer
        );

        CollectionFunctions.forEach(
                summaries,
                System.out::println
        );
    }
}