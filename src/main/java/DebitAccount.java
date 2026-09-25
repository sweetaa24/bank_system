public class DebitAccount extends BankAccount{
    public DebitAccount(String number, String owner, double initialBalance) {
        super(number, owner, initialBalance);
    }

    @Override
    public boolean withdraw(double amount){
        if (!canWithdraw(amount)) {
            return false;
        }
        decreaseBalance(amount);
        return true;
    }
}
