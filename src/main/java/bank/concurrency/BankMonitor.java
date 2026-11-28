package bank.concurrency;

import bank.domain.BankAccount;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BankMonitor implements Runnable {

    private final List<BankAccount> ordered;
    private final double expectedTotal;
    private final long intervalMillis;

    private volatile boolean running = true;

    public BankMonitor(
            List<BankAccount> accounts,
            double expectedTotal,
            long intervalMillis) {

        this.ordered = new ArrayList<>(accounts);
        this.ordered.sort(Comparator.comparing(
                account -> account.getId().value()));
        this.expectedTotal = expectedTotal;
        this.intervalMillis = intervalMillis;
    }

    public void stop() {
        running = false;
    }

    @Override
    public void run() {
        while (running) {
            check();

            try {
                Thread.sleep(intervalMillis);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        System.out.println("[MONITOR] stopped");
    }

    private void check() {
        double total = consistentTotal(0);

        System.out.println("[MONITOR] total="
                + format(total)
                + (total == expectedTotal ? " OK" : " ERROR"));
    }

    private double consistentTotal(int index) {
        if (index == ordered.size()) {
            return sum();
        }

        synchronized (ordered.get(index)) {
            return consistentTotal(index + 1);
        }
    }

    private double sum() {
        double total = 0;

        for (BankAccount account : ordered) {
            total += account.getBalance();
        }

        return total;
    }

    private static String format(double value) {
        if (value == Math.rint(value)) {
            return String.valueOf((long) value);
        }

        return String.valueOf(value);
    }
}
