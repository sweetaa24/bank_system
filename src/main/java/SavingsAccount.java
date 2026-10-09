public class SavingsAccount extends BankAccount{
    private final double minimumBalance;

    public SavingsAccount(String number, String owner, double initialBalance, double minimumBalance) {
        super(number, owner, initialBalance);
        this.minimumBalance = minimumBalance;
    }

    @Override
    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException(
                    "Amount must be positive"
            );
        }

        if (getBalance() - amount < minimumBalance) {
            throw new InsufficientFundsException(
                    "Insufficient funds"
            );
        }

        decreaseBalance(amount);
    }
}
