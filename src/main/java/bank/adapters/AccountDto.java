package bank.adapters;

import bank.domain.BankAccount;

public record AccountDto(
        String number,
        String owner,
        double balance,
        String type
) {
    public static AccountDto fromDomain(BankAccount account) {
        return new AccountDto(
                account.getId().value(),
                account.getOwner(),
                account.getBalance(),
                account.getClass().getSimpleName()
        );
    }
}
