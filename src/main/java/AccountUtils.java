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
}
