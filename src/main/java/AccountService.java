import java.util.Optional;

public class AccountService {
    private final Repository<AccountNumber, BankAccount> accounts;

    public AccountService(Repository<AccountNumber, BankAccount> accounts) {
        this.accounts = accounts;
    }

    public void openAccount(BankAccount account) {
        if (account == null) {
            throw new IllegalArgumentException("Account must not be null");
        }
        accounts.save(account);
    }

    public Optional<BankAccount> findAccount(AccountNumber number) {
        return Optional.ofNullable(accounts.findById(number));
    }

    public boolean closeAccount(AccountNumber number) {
        return accounts.deleteById(number);
    }
}
