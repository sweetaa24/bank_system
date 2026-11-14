package bank.application;

import bank.domain.AccountNumber;
import bank.domain.BankAccount;
import bank.domain.CommissionPolicy;
import bank.domain.Transaction;
import bank.domain.TransactionStatus;
import bank.domain.TransactionType;
import bank.domain.TransferLimitExceededException;
import bank.ports.BlockedAccountChecker;
import bank.ports.NotificationService;
import bank.ports.TransactionRecorder;

import java.util.concurrent.atomic.AtomicLong;

public class TransferService {
    private final CommissionPolicy commissionPolicy;
    private final NotificationService notificationService;
    private final BlockedAccountChecker blockedAccounts;
    private final TransactionRecorder journal;
    private static final AtomicLong TRANSACTION_IDS = new AtomicLong(1);

    public TransferService(
            CommissionPolicy commissionPolicy,
            NotificationService notificationService,
            BlockedAccountChecker blockedAccounts,
            TransactionRecorder journal
    ) {
        this.commissionPolicy = commissionPolicy;
        this.notificationService = notificationService;
        this.blockedAccounts = blockedAccounts;
        this.journal = journal;
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

        journal.add(new Transaction(
                TRANSACTION_IDS.getAndIncrement(),
                TransactionType.TRANSFER,
                from.getId(),
                amount,
                TransactionStatus.SUCCESS
        ));

        notificationService.notify(
                "Transfer " + amount
        );
    }

    public boolean isBlocked(AccountNumber accountNumber) {
        return blockedAccounts.isBlocked(accountNumber);
    }
}
