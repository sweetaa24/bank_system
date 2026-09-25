public abstract class BankAccount {
    private final String number;
    private final String owner;
    private double balance;

    protected BankAccount(String number, String owner, double initialBalance){
        this.number = number;
        this.owner = owner;
        if (initialBalance >= 0) {
            this.balance = initialBalance;
        } else {
            this.balance = 0;
        }
    }

    public void deposit(double amount){
        if (amount > 0){
            balance += amount;
        }
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
}
