import java.util.HashMap;
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
}
