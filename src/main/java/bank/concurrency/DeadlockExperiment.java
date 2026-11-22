package bank.concurrency;

import bank.application.TransferService;
import bank.domain.BankAccount;
import bank.domain.DebitAccount;
import bank.domain.NoCommission;

import java.util.concurrent.atomic.AtomicInteger;

public class DeadlockExperiment {

    private static final int UNORDERED_OPERATIONS = 1000;
    private static final int ORDERED_OPERATIONS = 50_000;
    private static final long LOCK_DELAY_MILLIS = 50;
    private static final long WAIT_LIMIT_MILLIS = 2000;

    public static void main(String[] args)
            throws InterruptedException {

        runUnorderedLocking();
        System.out.println();
        runOrderedLocking();
    }

    private static void runUnorderedLocking()
            throws InterruptedException {

        System.out.println(
                "Эксперимент 1: замки берутся в порядке аргументов");

        BankAccount a = new DebitAccount("1111111111", "A", 100_000);
        BankAccount b = new DebitAccount("2222222222", "B", 100_000);

        Thread first = unorderedWorker("Thread 1", a, b);
        Thread second = unorderedWorker("Thread 2", b, a);

        first.start();
        second.start();

        first.join(WAIT_LIMIT_MILLIS);
        second.join(WAIT_LIMIT_MILLIS);

        if (first.isAlive() && second.isAlive()) {
            System.out.println("DEADLOCK: оба потока живы и не двигаются");
            System.out.println("circular wait: Thread 1 держит A и ждёт B,"
                    + " Thread 2 держит B и ждёт A");
        } else {
            System.out.println("На этот раз оба потока прошли:"
                    + " deadlock проявляется не в каждом запуске");
        }
    }

    private static Thread unorderedWorker(
            String name,
            BankAccount from,
            BankAccount to) {

        Thread thread = new Thread(() -> {
            for (int i = 0; i < UNORDERED_OPERATIONS; i++) {
                unorderedTransfer(from, to, 100);
            }
        }, name);

        thread.setDaemon(true);
        return thread;
    }

    private static void unorderedTransfer(
            BankAccount from,
            BankAccount to,
            double amount) {

        synchronized (from) {
            log("locked " + from.getOwner());

            sleep(LOCK_DELAY_MILLIS);

            log("waits for " + to.getOwner());

            synchronized (to) {
                from.withdraw(amount);
                to.deposit(amount);
            }
        }
    }

    private static void runOrderedLocking()
            throws InterruptedException {

        System.out.println(
                "Эксперимент 2: TransferService с упорядоченным захватом");

        BankAccount a = new DebitAccount("1111111111", "A", 100_000);
        BankAccount b = new DebitAccount("2222222222", "B", 100_000);

        TransferService service = new TransferService(
                new NoCommission(),
                message -> {},
                number -> false,
                transaction -> {}
        );

        AtomicInteger failures = new AtomicInteger();

        Thread first = new Thread(
                () -> repeatTransfer(service, a, b, failures),
                "Thread 1");

        Thread second = new Thread(
                () -> repeatTransfer(service, b, a, failures),
                "Thread 2");

        long started = System.currentTimeMillis();

        first.start();
        second.start();

        first.join();
        second.join();

        long elapsed = System.currentTimeMillis() - started;

        System.out.println("Оба потока завершились за "
                + elapsed + " мс");
        System.out.println("Операций всего: "
                + (2 * ORDERED_OPERATIONS)
                + ", неуспешных: " + failures.get());
        System.out.println("Сумма A + B = "
                + (a.getBalance() + b.getBalance())
                + " (ожидается 200000.0)");
    }

    private static void repeatTransfer(
            TransferService service,
            BankAccount from,
            BankAccount to,
            AtomicInteger failures) {

        for (int i = 0; i < ORDERED_OPERATIONS; i++) {
            try {
                service.transfer(from, to, 1);
            } catch (RuntimeException e) {
                failures.incrementAndGet();
            }
        }
    }

    private static void log(String message) {
        System.out.println(
                Thread.currentThread().getName() + " " + message);
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
