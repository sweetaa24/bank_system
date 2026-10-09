public class TransferService {
    private final CommissionPolicy commissionPolicy;
    private final NotificationService notificationService;

    public TransferService(CommissionPolicy commissionPolicy, NotificationService notificationService) {
        this.commissionPolicy = commissionPolicy;
        this.notificationService = notificationService;
    }

    public void transfer(
            BankAccount from,
            BankAccount to,
            double amount) {

        if (amount <= 0) {
            throw new IllegalArgumentException(
                    "Amount must be positive"
            );
        }

        if (from.equals(to)) {
            throw new IllegalArgumentException(
                    "Cannot transfer to the same account"
            );
        }

        if (amount > 50_000) {
            throw new TransferLimitExceededException(
                    "Transfer limit exceeded"
            );
        }

        double commission = commissionPolicy.calculate(amount);
        double totalAmount = amount + commission;

        from.withdraw(totalAmount);
        to.deposit(amount);

        notificationService.notify(
                "Transfer " + amount
        );
    }
}
