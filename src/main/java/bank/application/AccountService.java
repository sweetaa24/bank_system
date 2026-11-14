package bank.application;

import bank.domain.AccountNumber;
import bank.domain.BankAccount;
import bank.ports.AccountRepository;

import java.util.Optional;

public class AccountService {
    private final AccountRepository accounts;

    public AccountService(AccountRepository accounts) {
        this.accounts = accounts;
    }

    public void openAccount(BankAccount account) {
        if (account == null) {
            throw new IllegalArgumentException("Account must not be null");
        }
        accounts.save(account);
    }

    public Optional<BankAccount> findAccount(AccountNumber number) {
        return accounts.findById(number);
    }

    public boolean closeAccount(AccountNumber number) {
        return accounts.deleteById(number);
    }
}
