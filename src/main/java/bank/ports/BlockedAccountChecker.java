package bank.ports;

import bank.domain.AccountNumber;

public interface BlockedAccountChecker {
    boolean isBlocked(AccountNumber number);
}