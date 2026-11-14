
import java.util.List;
import java.util.function.Predicate;
import java.util.Comparator;
import java.util.Optional;
import java.util.OptionalDouble;

public class BankService {
    private final AccountService accountService;
    private final TransferApplicationService transferApplicationService;
    private final BankReportService bankReportService;

    public BankService(
            Repository<AccountNumber, BankAccount> accounts,
            TransferService transferService) {

        this.accountService = new AccountService(accounts);

        this.transferApplicationService =
                new TransferApplicationService(accounts, transferService);

        this.bankReportService =
                new BankReportService(accounts, transferService::isBlocked);
    }

    public void openAccount(BankAccount account) {
        accountService.openAccount(account);
    }

    public BankAccount findAccount(AccountNumber number) {
        return accountService.findAccount(number).orElse(null);
    }

    public boolean closeAccount(AccountNumber number) {
        return accountService.closeAccount(number);
    }

    public void transfer(
            AccountNumber fromNumber,
            AccountNumber toNumber,
            double amount) {
        transferApplicationService.transfer(fromNumber, toNumber, amount);
    }

    public List<BankAccount> findAccounts(
            Predicate<? super BankAccount> condition) {
        return bankReportService.findAccounts(condition);
    }

    public List<BankAccount> sortedAccounts(
            Comparator<? super BankAccount> comparator) {
        return bankReportService.sortedAccounts(comparator);
    }

    public List<BankAccount> findRichAccounts(double minBalance) {
        return bankReportService.findRichAccounts(minBalance);
    }

    public List<BankAccount> findDebitAccounts() {
        return bankReportService.findDebitAccounts();
    }

    public List<BankAccount> findAccountsByOwner(String owner) {
        return bankReportService.findAccountsByOwner(owner);
    }

    public List<AccountSummary> getAccountSummaries() {
        return bankReportService.getAccountSummaries();
    }

    public List<BankAccount> richestFirst() {
        return bankReportService.richestFirst();
    }

    public List<BankAccount> accountsByNumber() {
        return bankReportService.accountsByNumber();
    }

    public List<BankAccount> accountsByTypeThenBalance() {
        return bankReportService.accountsByTypeThenBalance();
    }

    public List<BankAccount> accountsByOwnerThenNumber() {
        return bankReportService.accountsByOwnerThenNumber();
    }

    public boolean hasBlockedAccounts() {
        return bankReportService.hasBlockedAccounts();
    }

    public boolean allAccountsPositive() {
        return bankReportService.allAccountsPositive();
    }

    public boolean noCreditAccounts() {
        return bankReportService.noCreditAccounts();
    }

    public Optional<BankAccount> findFirstRichAccount(double minBalance) {
        return bankReportService.findFirstRichAccount(minBalance);
    }

    public double totalBalance() {
        return bankReportService.totalBalance();
    }

    public OptionalDouble averageBalance() {
        return bankReportService.averageBalance();
    }

    public Optional<BankAccount> richestAccount() {
        return bankReportService.richestAccount();
    }
}
