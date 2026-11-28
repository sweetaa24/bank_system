package bank.concurrency;

import java.util.concurrent.atomic.AtomicInteger;

public class BankStatistics {

    private final AtomicInteger successfulTransfers =
            new AtomicInteger();

    private final AtomicInteger failedTransfers =
            new AtomicInteger();

    public void recordSuccess() {
        successfulTransfers.incrementAndGet();
    }

    public void recordFailure() {
        failedTransfers.incrementAndGet();
    }

    public int successfulTransfers() {
        return successfulTransfers.get();
    }

    public int failedTransfers() {
        return failedTransfers.get();
    }

    public int recordedTotal() {
        return successfulTransfers() + failedTransfers();
    }
}
