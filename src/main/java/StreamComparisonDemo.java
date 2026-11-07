import java.util.ArrayList;
import java.util.List;

public class StreamComparisonDemo {

    public static void main(String[] args) {
        List<BankAccount> accounts = List.of(
                new DebitAccount("1111111111", "Ivan", 150000),
                new DebitAccount("2222222222", "Anna", 50000),
                new CreditAccount("3333333333", "Petr", 200000, 50000),
                new SavingsAccount("4444444444", "Maria", 120000, 1000)
        );

        List<BankAccount> result = new ArrayList<>();

        for (BankAccount account : accounts) {
            if (account.getBalance() >= 100_000) {
                result.add(account);
            }
        }

        System.out.println("Результат через цикл:");
        System.out.println(result);

        List<BankAccount> streamResult =
                accounts.stream()
                        .filter(a -> a.getBalance() >= 100_000)
                        .toList();

        System.out.println("Результат через Stream API:");
        System.out.println(streamResult);
    }
}