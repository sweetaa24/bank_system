package bank.application;

import bank.domain.AccountNumber;
import bank.domain.BankAccount;
import bank.domain.CreditAccount;
import bank.domain.DebitAccount;
import bank.ports.AccountRepository;

import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.function.Predicate;

public class BankReportService {
    private final AccountRepository accounts;
    private final Predicate<AccountNumber> isBlocked;

    public BankReportService(
            AccountRepository accounts,
            Predicate<AccountNumber> isBlocked) {
        this.accounts = accounts;
        this.isBlocked = isBlocked;
    }

    public List<BankAccount> findAccounts(
            Predicate<? super BankAccount> condition) {
        return new ArrayList<>(
                accounts.findAll().stream()
                        .filter(condition)
                        .toList()
        );
    }

    public List<BankAccount> sortedAccounts(
            Comparator<? super BankAccount> comparator) {
        List<BankAccount> result = new ArrayList<>(accounts.findAll());
        result.sort(comparator);
        return result;
    }

    public List<BankAccount> findRichAccounts(double minBalance) {
        return accounts.findAll().stream()
                .filter(a -> a.getBalance() >= minBalance)
                .toList();
    }

    public List<BankAccount> findDebitAccounts() {
        return accounts.findAll().stream()
                .filter(DebitAccount.class::isInstance)
                .toList();
    }

    public List<BankAccount> findAccountsByOwner(String owner) {
        return accounts.findAll().stream()
                .filter(a -> a.getOwner().equals(owner))
                .toList();
    }

    public List<AccountSummary> getAccountSummaries() {
        return accounts.findAll().stream()
                .map(a -> new AccountSummary(
                        a.getId(),
                        a.getClass().getSimpleName(),
                        a.getBalance()))
                .toList();
    }

    public List<BankAccount> richestFirst() {
        return accounts.findAll().stream()
                .sorted(Comparator.comparingDouble(
                        BankAccount::getBalance).reversed())
                .toList();
    }

    public List<BankAccount> accountsByNumber() {
        return accounts.findAll().stream()
                .sorted(Comparator.comparing(a -> a.getId().value()))
                .toList();
    }

    public List<BankAccount> accountsByTypeThenBalance() {
        return accounts.findAll().stream()
                .sorted(Comparator.comparing(
                                (BankAccount a) -> a.getClass().getSimpleName())
                        .thenComparingDouble(BankAccount::getBalance))
                .toList();
    }

    public List<BankAccount> accountsByOwnerThenNumber() {
        return accounts.findAll().stream()
                .sorted(Comparator.comparing(BankAccount::getOwner)
                        .thenComparing(a -> a.getId().value()))
                .toList();
    }

    public boolean hasBlockedAccounts() {
        return accounts.findAll().stream()
                .anyMatch(a -> isBlocked.test(a.getId()));
    }

    public boolean allAccountsPositive() {
        return accounts.findAll().stream()
                .allMatch(a -> a.getBalance() > 0);
    }

    public boolean noCreditAccounts() {
        return accounts.findAll().stream()
                .noneMatch(CreditAccount.class::isInstance);
    }

    public Optional<BankAccount> findFirstRichAccount(double minBalance) {
        return accounts.findAll().stream()
                .filter(a -> a.getBalance() >= minBalance)
                .findFirst();
    }

    public double totalBalance() {
        return accounts.findAll().stream()
                .mapToDouble(BankAccount::getBalance)
                .sum();
    }

    public OptionalDouble averageBalance() {
        return accounts.findAll().stream()
                .mapToDouble(BankAccount::getBalance)
                .average();
    }

    public Optional<BankAccount> richestAccount() {
        return accounts.findAll().stream()
                .max(Comparator.comparingDouble(BankAccount::getBalance));
    }
}
