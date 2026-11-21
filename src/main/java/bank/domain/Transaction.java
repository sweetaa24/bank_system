package bank.domain;

public record Transaction(
        Long id,
        TransactionType type,
        AccountNumber account,
        AccountNumber to,
        double amount,
        TransactionStatus status)
        implements Identifiable<Long>{
    @Override
    public Long getId() {
        return id;
    }
}