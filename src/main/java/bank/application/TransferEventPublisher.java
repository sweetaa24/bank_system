package bank.application;

import bank.domain.Transaction;
import bank.ports.TransferListener;

import java.util.ArrayList;
import java.util.List;

public class TransferEventPublisher {
    private final List<TransferListener> listeners = new ArrayList<>();

    public void subscribe(TransferListener listener) {
        listeners.add(listener);
    }

    public void publish(Transaction tx) {
        for (TransferListener listener : listeners) {
            listener.onTransferCompleted(tx);
        }
    }
}
