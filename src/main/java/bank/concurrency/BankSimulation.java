package bank.concurrency;

import bank.application.TransferService;
import bank.domain.BankAccount;
import bank.domain.DebitAccount;
import bank.domain.NoCommission;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class BankSimulation {

    private static final int CLIENTS = 5;
    private static final int OPERATIONS_PER_CLIENT = 2000;
    private static final int MAX_AMOUNT = 20_000;
    private static final double INITIAL_BALANCE = 100_000;
    private static final long MONITOR_INTERVAL_MILLIS = 5;

    public static void main(String[] args)
            throws InterruptedException {

        List<BankAccount> accounts = createAccounts();

        double initialTotal = total(accounts);

        TransferService transferService = new TransferService(
                new NoCommission(),
                message -> {},
                number -> false,
                transaction -> {}
        );

        BankMonitor monitor = new BankMonitor(
                accounts,
                initialTotal,
                MONITOR_INTERVAL_MILLIS
        );

        Thread monitorThread = new Thread(monitor, "monitor");
        monitorThread.start();

        AtomicInteger successes = new AtomicInteger();
        AtomicInteger failures = new AtomicInteger();

        List<Thread> threads = new ArrayList<>();

        for (int i = 1; i <= CLIENTS; i++) {
            String name = "client-" + i;

            Thread thread = new Thread(
                    new TransferClient(
                            name,
                            accounts,
                            transferService,
                            OPERATIONS_PER_CLIENT,
                            MAX_AMOUNT,
                            successes,
                            failures
                    ),
                    name
            );

            threads.add(thread);
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        monitor.stop();
        monitorThread.interrupt();
        monitorThread.join();

        printReport(accounts, initialTotal, successes, failures);
    }

    private static List<BankAccount> createAccounts() {
        List<BankAccount> accounts = new ArrayList<>();

        accounts.add(new DebitAccount("1111111111", "A", INITIAL_BALANCE));
        accounts.add(new DebitAccount("2222222222", "B", INITIAL_BALANCE));
        accounts.add(new DebitAccount("3333333333", "C", INITIAL_BALANCE));
        accounts.add(new DebitAccount("4444444444", "D", INITIAL_BALANCE));
        accounts.add(new DebitAccount("5555555555", "E", INITIAL_BALANCE));

        return accounts;
    }

    private static double total(List<BankAccount> accounts) {
        double sum = 0;

        for (BankAccount account : accounts) {
            sum += account.getBalance();
        }

        return sum;
    }

    private static void printReport(
            List<BankAccount> accounts,
            double initialTotal,
            AtomicInteger successes,
            AtomicInteger failures) {

        System.out.println("Итоговые балансы:");

        for (BankAccount account : accounts) {
            System.out.println("  "
                    + account.getOwner() + " ("
                    + account.getId().value() + ") = "
                    + account.getBalance());
        }

        double finalTotal = total(accounts);

        System.out.println("INITIAL_TOTAL = " + initialTotal);
        System.out.println("FINAL_TOTAL   = " + finalTotal);
        System.out.println("Расхождение   = "
                + (finalTotal - initialTotal));
        System.out.println("INITIAL_TOTAL == FINAL_TOTAL: "
                + (initialTotal == finalTotal));
        System.out.println("Успешных операций:   " + successes.get());
        System.out.println("Неуспешных операций: " + failures.get());
    }
}
