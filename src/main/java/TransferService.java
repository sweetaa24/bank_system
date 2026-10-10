public class TransferService {
    private final CommissionPolicy commissionPolicy;
    private final NotificationService notificationService;
    private final BlockedAccountRegistry blockedAccounts;

    public TransferService(CommissionPolicy commissionPolicy, NotificationService notificationService, BlockedAccountRegistry blockedAccounts) {
        this.commissionPolicy = commissionPolicy;
        this.notificationService = notificationService;
        this.blockedAccounts = blockedAccounts;
    }

    public void transfer(
            BankAccount from,
            BankAccount to,
            double amount) {

        if (blockedAccounts.isBlocked(from.getId())) {
            throw new IllegalStateException("Source account is blocked");
        }

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
