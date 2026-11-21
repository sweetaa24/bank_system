package bank.adapters;

import bank.domain.Transaction;

public record TransactionDto(
        long id,
        String from,
        String to,
        double amount,
        String status
) {
    public static TransactionDto fromDomain(Transaction transaction) {
        return new TransactionDto(
                transaction.id(),
                transaction.account().value(),
                transaction.to() == null ? "" : transaction.to().value(),
                transaction.amount(),
                transaction.status().name()
        );
    }
}
