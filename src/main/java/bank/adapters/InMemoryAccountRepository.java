package bank.adapters;

import bank.domain.AccountNumber;
import bank.domain.BankAccount;
import bank.ports.AccountRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryAccountRepository
        implements AccountRepository {

    private final Map<AccountNumber, BankAccount> data =
            new HashMap<>();

    @Override
    public Optional<BankAccount> findById(AccountNumber number) {
        return Optional.ofNullable(data.get(number));
    }

    @Override
    public void save(BankAccount account) {
        data.put(account.getId(), account);
    }

    @Override
    public boolean deleteById(AccountNumber number) {
        return data.remove(number) != null;
    }

    @Override
    public List<BankAccount> findAll() {
        return new ArrayList<>(data.values());
    }
}
