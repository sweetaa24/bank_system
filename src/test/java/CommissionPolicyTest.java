
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommissionPolicyTest {

    @Test
    void fixedCommissionReturnsSameAmount() {
        CommissionPolicy policy = new FixedCommissionPolicy(50);

        assertEquals(50, policy.calculate(1000));
        assertEquals(50, policy.calculate(10000));
    }

    @Test
    void fixedCommissionRejectsNegativeValue() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new FixedCommissionPolicy(-50)
        );
    }

    @Test
    void progressiveCommissionCalculatesCorrectly() {
        CommissionPolicy policy =
                new ProgressiveCommissionPolicy(10000, 1, 2);

        assertEquals(50, policy.calculate(5000));
        assertEquals(100, policy.calculate(10000));
        assertEquals(300, policy.calculate(20000));
        assertEquals(500, policy.calculate(30000));
    }

    @Test
    void progressiveCommissionRejectsNegativeParameters() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new ProgressiveCommissionPolicy(10000, -1, 2)
        );
    }


    @Test
    void fixedCommissionWorksWithTransferService() {
        BankAccount from = new DebitAccount(
                "1111111111", "Ivan", 1000
        );
        BankAccount to = new DebitAccount(
                "2222222222", "Petr", 0
        );

        TransferService service = new TransferService(
                new FixedCommissionPolicy(50),
                message -> {},
                new BlockedAccountRegistry(),
                new TransactionJournal()
        );

        service.transfer(from, to, 100);

        assertEquals(850, from.getBalance());
        assertEquals(100, to.getBalance());
    }

    @Test
    void progressiveCommissionWorksWithTransferService() {
        BankAccount from = new DebitAccount(
                "3333333333", "Anna", 20000
        );
        BankAccount to = new DebitAccount(
                "4444444444", "Petr", 0
        );

        TransferService service = new TransferService(
                new ProgressiveCommissionPolicy(10000, 1, 2),
                message -> {},
                new BlockedAccountRegistry(),
                new TransactionJournal()
        );

        service.transfer(from, to, 15000);

        assertEquals(4800, from.getBalance());
        assertEquals(15000, to.getBalance());
    }

}
