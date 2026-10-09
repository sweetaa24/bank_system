public class CreditAccount extends BankAccount{
    private final double creditLimit;

    public CreditAccount(String number, String owner, double initialBalance, double creditLimit) {
        super(number, owner, initialBalance);
        this.creditLimit = creditLimit;
    }

    @Override
    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException(
                    "Amount must be positive"
            );
        }

        if (getBalance() - amount < -creditLimit) {
            throw new InsufficientFundsException(
                    "Insufficient funds"
            );
        }

        decreaseBalance(amount);
    }
}
