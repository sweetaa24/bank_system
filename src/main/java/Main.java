public class Main {
    public static void main(String[] args) {
//        BankAccount from = new DebitAccount(
//                "1234567890", "Иван", 1000
//        );
//
//        BankAccount to = new DebitAccount(
//                "0987654321", "Анна", 2000
//        );
//
//        TransferService transferService = new TransferService(
//                new NoCommission(),
//                new ConsoleNotificationService()
//        );
//
//        try {
//            transferService.transfer(from, to, 5000);
//
//            System.out.println("Transfer completed");
//        } catch (InsufficientFundsException e) {
//            System.out.println(
//                    "Transfer failed: " + e.getMessage()
//            );
//        }
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

        String[] names = {"Ivan", "Anna", "Petr"};

        System.out.println(ArrayUtils.first(names));
        System.out.println(ArrayUtils.last(names));
        System.out.println(ArrayUtils.contains(names, "Anna"));
        System.out.println(ArrayUtils.contains(names, "Oleg"));
    }
}