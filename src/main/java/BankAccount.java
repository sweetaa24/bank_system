public abstract class BankAccount {
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

    public void deposit(double amount){
        if (amount <= 0){
            throw new IllegalArgumentException("Amount must be positive");
        }
        balance += amount;
    }
    public abstract boolean withdraw(double amount);

    public double getBalance(){
        return balance;
    }

    protected boolean canWithdraw(double amount){
        return amount > 0 && balance >= amount;
    }

    protected void decreaseBalance(double amount) {
        balance -= amount;
    }
    @Override
    public String toString() {
        return getClass().getSimpleName() + "{\n" +
                " number='" + number + "',\n" +
                " owner='" + owner + "',\n" +
                " balance=" + balance + "\n" +
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
