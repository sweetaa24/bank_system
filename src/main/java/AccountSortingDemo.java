
import java.util.Comparator;
import java.util.List;

public class AccountSortingDemo {

    public static void main(String[] args) {
        List<BankAccount> accounts = List.of(
                new DebitAccount("1111111111", "Анна", 5000),
                new DebitAccount("2222222222", "Иван", 1500),
                new DebitAccount("3333333333", "Мария", 8000)
        );

        // 1. Анонимный класс
        Comparator<BankAccount> byBalance =
                new Comparator<>() {
                    @Override
                    public int compare(BankAccount a, BankAccount b) {
                        return Double.compare(
                                a.getBalance(), b.getBalance()
                        );
                    }
                };

        accounts = new java.util.ArrayList<>(accounts);
        accounts.sort(byBalance);
        System.out.println("Анонимный класс: " + accounts);

        // 2. Lambda
        Comparator<BankAccount> byBalanceLambda =
                (a, b) -> Double.compare(
                        a.getBalance(), b.getBalance()
                );

        accounts.sort(byBalanceLambda);
        System.out.println("Lambda: " + accounts);

        // 3. Фабричный метод Comparator
        Comparator<BankAccount> byBalanceMethod =
                Comparator.comparingDouble(BankAccount::getBalance);

        accounts.sort(byBalanceMethod);
        System.out.println("Фабричный метод: " + accounts);
    }
}
