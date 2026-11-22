package bank.concurrency;

import bank.application.TransferService;
import bank.domain.BankAccount;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class TransferClient implements Runnable {

    private final String name;
    private final List<BankAccount> accounts;
    private final TransferService transferService;
    private final int operations;
    private final int maxAmount;
    private final AtomicInteger successes;
    private final AtomicInteger failures;
    private final Random random = new Random();

    public TransferClient(
            String name,
            List<BankAccount> accounts,
            TransferService transferService,
            int operations,
            int maxAmount,
            AtomicInteger successes,
            AtomicInteger failures) {

        this.name = name;
        this.accounts = accounts;
        this.transferService = transferService;
        this.operations = operations;
        this.maxAmount = maxAmount;
        this.successes = successes;
        this.failures = failures;
    }

    public String getName() {
        return name;
    }

    @Override
    public void run() {
        for (int i = 0; i < operations; i++) {
            try {
                transferOnce();
                successes.incrementAndGet();
            } catch (RuntimeException e) {
                failures.incrementAndGet();
            }
        }
    }

    private void transferOnce() {
        int fromIndex = random.nextInt(accounts.size());
        int toIndex = random.nextInt(accounts.size() - 1);

        if (toIndex >= fromIndex) {
            toIndex++;
        }

        double amount = 1 + random.nextInt(maxAmount);

        transferService.transfer(
                accounts.get(fromIndex),
                accounts.get(toIndex),
                amount
        );
    }
}
