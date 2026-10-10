import java.util.List;

public class AccountUtils {

    public static double totalBalance(
            List<? extends BankAccount> accounts) {

        double total = 0;

        for (BankAccount account : accounts) {
            total += account.getBalance();
        }

        return total;
    }

    public static void addDemoDebitAccounts(
            List<? super DebitAccount> target) {

        target.add(new DebitAccount(
                "1234567890", "Ivan", 1000
        ));

        target.add(new DebitAccount(
                "0987654321", "Anna", 2000
        ));
    }
    public static <T> void copy(
            List<? extends T> source,
            List<? super T> target) {

        for (T value : source) {
            target.add(value);
        }
    }

    public static <T extends BankAccount> T richest(
            List<T> accounts) {

        if (accounts.isEmpty()) {
            throw new IllegalArgumentException(
                    "Account list cannot be empty"
            );
        }

        T richest = accounts.get(0);

        for (T account : accounts) {
            if (account.getBalance() > richest.getBalance()) {
                richest = account;
            }
        }

        return richest;
    }
}
