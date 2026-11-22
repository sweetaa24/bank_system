package bank.domain;

public abstract class BankAccount
        implements Identifiable<AccountNumber>{
    private final AccountNumber number;
    private final String owner;
    private double balance;

    protected BankAccount(String number, String owner, double initialBalance){
        this.number = new AccountNumber(number);
        this.owner = owner;
        if (initialBalance >= 0) {
            this.balance = initialBalance;
        } else {
            this.balance = 0;
        }
    }

    @Override
    public AccountNumber getId() {
        return number;
    }

    public String getOwner() {
        return owner;
    }

    public synchronized void deposit(double amount){
        if (amount <= 0){
            throw new IllegalArgumentException("Amount must be positive");
        }
        balance += amount;
    }

    public abstract void withdraw(double amount);

    public synchronized double getBalance(){
        return balance;
    }

    protected synchronized void decreaseBalance(double amount) {
        balance -= amount;
    }
    @Override
    public String toString() {
        return getClass().getSimpleName() + "{\n" +
                " number='" + number + "',\n" +
                " owner='" + owner + "',\n" +
                " balance=" + getBalance() + "\n" +
                "}";
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof BankAccount)) {
            return false;
        }

        BankAccount that = (BankAccount) o;
        return number.equals(that.number);
    }

    @Override
    public int hashCode() {
        return number.hashCode();
    }
}
