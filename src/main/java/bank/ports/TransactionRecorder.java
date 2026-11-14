package bank.ports;

import bank.domain.Transaction;

public interface TransactionRecorder {
    void add(Transaction transaction);
}