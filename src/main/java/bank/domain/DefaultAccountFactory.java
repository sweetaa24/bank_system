package bank.domain;

public class DefaultAccountFactory
        implements AccountFactory {

    @Override
    public BankAccount create(
            AccountType type,
            AccountNumber number,
            String owner,
            double balance) {

        return switch (type) {
            case DEBIT ->
                    new DebitAccount(number.value(), owner, balance);
            case SAVINGS ->
                    new SavingsAccount(number.value(), owner, balance, 1000);
            case CREDIT ->
                    new CreditAccount(number.value(), owner, balance, 50_000);
        };
    }
}
