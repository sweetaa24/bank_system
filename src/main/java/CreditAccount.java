public class CreditAccount extends BankAccount{
    private final double creditLimit;

    public CreditAccount(String number, String owner, double initialBalance, double creditLimit) {
        super(number, owner, initialBalance);
        this.creditLimit = creditLimit;
    }

    @Override
    public boolean withdraw(double amount){
        if (getBalance() - amount < -creditLimit){
            return false;
        }
        decreaseBalance(amount);
        return true;
    }
}
