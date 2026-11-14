package bank.application;

import bank.domain.AccountNumber;
import bank.domain.BankAccount;
import bank.ports.AccountRepository;

public class TransferApplicationService {
    private final AccountRepository accounts;
    private final TransferService transferService;

    public TransferApplicationService(
            AccountRepository accounts,
            TransferService transferService) {
        this.accounts = accounts;
        this.transferService = transferService;
    }

    public void transfer(
            AccountNumber fromNumber,
            AccountNumber toNumber,
            double amount) {

        BankAccount from = accounts.findById(fromNumber)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Sender account not found"));

        BankAccount to = accounts.findById(toNumber)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Recipient account not found"));

        transferService.transfer(from, to, amount);
    }
}
