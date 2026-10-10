import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        BankAccount from = new DebitAccount(
                "1234567890", "Иван", 1000
        );

        BankAccount to = new DebitAccount(
                "0987654321", "Анна", 2000
        );

        TransactionJournal journal = new TransactionJournal();

        TransferService transferService = new TransferService(
                new NoCommission(),
                new ConsoleNotificationService(),
                new BlockedAccountRegistry(),
                journal
        );

        try {
            transferService.transfer(from, to, 5000);

            System.out.println("Transfer completed");
        } catch (InsufficientFundsException e) {
            System.out.println(
                    "Transfer failed: " + e.getMessage()
            );
        }

        System.out.println(from.getId());

//        Repository<AccountNumber, BankAccount> accounts;
//        Repository<Long, Transaction> transactions;

//        List<DebitAccount> debitAccounts = new ArrayList<>();
//        List<? extends BankAccount> accounts = debitAccounts;

        List<DebitAccount> debitAccounts = new ArrayList<>();

        debitAccounts.add(new DebitAccount(
                "1234567890", "Ivan", 1000
        ));

        debitAccounts.add(new DebitAccount(
                "0987654321", "Anna", 2000
        ));

//        System.out.println(AccountUtils.totalBalance(debitAccounts));
//
//        List<BankAccount> accounts = new ArrayList<>();
//
//        AccountUtils.addDemoDebitAccounts(accounts);
//
//        System.out.println(accounts.size());

//        List<String> strings = new ArrayList<>();
//        List<Integer> numbers = new ArrayList<>();
//
//        System.out.println(strings.getClass());
//        System.out.println(numbers.getClass());
//        System.out.println(
//                strings.getClass() == numbers.getClass()
//        );
//
//        System.out.println("Sender balance: " + from.getBalance());
//        System.out.println("Receiver balance: " + to.getBalance());

//        Pair<String, Integer> age =
//                new Pair<>("Ivan", 20);
//
//        AccountNumber accountNumber =
//                new AccountNumber("1234567890");
//
//        Pair<AccountNumber, String> owner =
//                new Pair<>(accountNumber, "Ivan");
//
//        System.out.println(age.key());
//        System.out.println(age.value());
//
//        System.out.println(owner.key());
//        System.out.println(owner.value());
//
//        String[] names = {"Ivan", "Anna", "Petr"};
//
//        System.out.println(ArrayUtils.first(names));
//        System.out.println(ArrayUtils.last(names));
//        System.out.println(ArrayUtils.contains(names, "Anna"));
//        System.out.println(ArrayUtils.contains(names, "Oleg"));


//        Box<String> text = new Box<>();
//        text.set("Java");
//        String s = text.get();
//        System.out.println(s);
//
//        Box<Integer> number = new Box<>();
//        number.set(42);
//        Integer n = number.get();
//        System.out.println(n);

    }
}