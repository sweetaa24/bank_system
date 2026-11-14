package bank.ports;

import bank.domain.Transaction;

public interface TransferListener {
    void onTransferCompleted(Transaction transaction);
}
