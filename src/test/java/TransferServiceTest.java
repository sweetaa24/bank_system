import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TransferServiceTest {

    @Test
    void successfulTransferChangesBothBalances() {
        BankAccount from = new DebitAccount("0000000001", "Иван", 10000);
        BankAccount to = new DebitAccount("0000000002", "Пётр", 2000);
        TransferService service = new TransferService(new NoCommission(), new ConsoleNotificationService());

        service.transfer(from, to, 3000);

        assertEquals(7000, from.getBalance());
        assertEquals(5000, to.getBalance());
    }

    @Test
    void failedTransferDoesNotChangeBalances() {
        BankAccount from = new DebitAccount("0000000001", "Иван", 1000);
        BankAccount to = new DebitAccount("0000000002", "Пётр", 2000);
        TransferService service = new TransferService(new NoCommission(), new FakeNotificationService());

        assertThrows(
                InsufficientFundsException.class,
                () -> service.transfer(from, to, 5000)
        );

        assertEquals(1000, from.getBalance());
        assertEquals(2000, to.getBalance());
    }

    @Test
    void negativeTransferIsForbidden() {
        BankAccount from = new DebitAccount("0000000001", "Иван", 10000);
        BankAccount to = new DebitAccount("0000000002", "Пётр", 2000);
        TransferService service = new TransferService(new NoCommission(), new ConsoleNotificationService());

        assertThrows(
                IllegalArgumentException.class,
                () -> service.transfer(from, to, -1000)
        );
    }

    @Test
    void zeroTransferIsForbidden() {
        BankAccount from = new DebitAccount("0000000001", "Иван", 10000);
        BankAccount to = new DebitAccount("0000000002", "Пётр", 2000);
        TransferService service = new TransferService(new NoCommission(), new ConsoleNotificationService());

        assertThrows(
                IllegalArgumentException.class,
                () -> service.transfer(from, to, 0)
        );
    }

    @Test
    void cannotTransferToSameAccount() {
        BankAccount account = new DebitAccount("0000000001", "Иван", 10000);
        TransferService service = new TransferService(new NoCommission(), new ConsoleNotificationService());

        assertThrows(
                IllegalArgumentException.class,
                () -> service.transfer(account, account, 1000)
        );

        assertEquals(10000, account.getBalance());
    }

    @Test
    void commissionIsChargedFromSender() {
        BankAccount from = new DebitAccount("0000000001", "Иван", 10000);
        BankAccount to = new DebitAccount("0000000002", "Пётр", 2000);
        TransferService service = new TransferService(new PercentCommission(10), new ConsoleNotificationService());

        service.transfer(from, to, 3000);
        assertEquals(6700, from.getBalance());
    }

    @Test
    void receiverGetsExactlyTransferAmount() {
        BankAccount from = new DebitAccount("0000000001", "Иван", 10000);
        BankAccount to = new DebitAccount("0000000002", "Пётр", 2000);
        TransferService service = new TransferService(new PercentCommission(10), new ConsoleNotificationService());

        service.transfer(from, to, 3000);

        assertEquals(5000, to.getBalance());
    }

    @Test
    void transferFailsWhenMoneyIsInsufficientIncludingCommission() {
        BankAccount from = new DebitAccount("0000000001", "Иван", 3000);
        BankAccount to = new DebitAccount("0000000002", "Пётр", 2000);
        TransferService service = new TransferService(new PercentCommission(10), new ConsoleNotificationService());

        assertThrows(
                InsufficientFundsException.class,
                () -> service.transfer(from, to, 3000)
        );

        assertEquals(3000, from.getBalance());
        assertEquals(2000, to.getBalance());
    }
    @Test
    void transferDebitToDebit() {
        BankAccount from = new DebitAccount("0000000001", "Иван", 10000);
        BankAccount to = new DebitAccount("0000000002", "Пётр", 2000);
        TransferService service = new TransferService(new NoCommission(), new ConsoleNotificationService());

        service.transfer(from, to, 3000);
        assertEquals(7000, from.getBalance());
        assertEquals(5000, to.getBalance());
    }

    @Test
    void transferDebitToSavings() {
        BankAccount from = new DebitAccount("0000000001", "Иван", 10000);
        BankAccount to = new SavingsAccount("0000000002", "Пётр", 2000, 1000);
        TransferService service = new TransferService(new NoCommission(), new ConsoleNotificationService());

        service.transfer(from, to, 3000);
        assertEquals(7000, from.getBalance());
        assertEquals(5000, to.getBalance());
    }

    @Test
    void transferCreditToDebit() {
        BankAccount from = new CreditAccount("0000000001", "Иван", 1000, 5000);
        BankAccount to = new DebitAccount("0000000002", "Пётр", 2000);

        TransferService service = new TransferService(new NoCommission(), new ConsoleNotificationService());
        service.transfer(from, to, 3000);
        assertEquals(-2000, from.getBalance());
        assertEquals(5000, to.getBalance());
    }

    @Test
    void transferSavingsToDebit() {
        BankAccount from = new SavingsAccount("0000000001", "Иван", 10000, 1000);
        BankAccount to = new DebitAccount("0000000002", "Пётр", 2000);

        TransferService service = new TransferService(new NoCommission(), new ConsoleNotificationService());

        service.transfer(from, to, 3000);

        assertEquals(7000, from.getBalance());
        assertEquals(5000, to.getBalance());
    }
    @Test
    void successfulTransferSendsOneNotification() {
        BankAccount from = new DebitAccount("0000000001", "Иван", 10000);
        BankAccount to = new DebitAccount("0000000002", "Пётр", 2000);

        FakeNotificationService notificationService = new FakeNotificationService();
        TransferService service = new TransferService(new NoCommission(), notificationService);

        service.transfer(from, to, 3000);
        assertEquals(1, notificationService.getNotificationCount());
    }

    @Test
    void failedTransferDoesNotSendNotification() {
        BankAccount from = new DebitAccount("0000000001", "Иван", 1000);
        BankAccount to = new DebitAccount("0000000002", "Пётр", 2000);

        FakeNotificationService notificationService = new FakeNotificationService();
        TransferService service = new TransferService(new NoCommission(), notificationService);

        assertThrows(
                InsufficientFundsException.class,
                () -> service.transfer(from, to, 2000)
        );

        assertEquals(0, notificationService.getNotificationCount());
    }

    @Test
    void notificationMessageIsCorrect() {
        BankAccount from = new DebitAccount("0000000001", "Иван", 10000);
        BankAccount to = new DebitAccount("0000000002", "Пётр", 2000);

        FakeNotificationService notificationService = new FakeNotificationService();
        TransferService service = new TransferService(new NoCommission(), notificationService);

        service.transfer(from, to, 3000);

        assertEquals("Transfer 3000.0", notificationService.getLastMessage());}

    @Test
    void transferOverLimitThrowsException() {
        BankAccount from = new DebitAccount(
                "1234567890", "Иван", 100000
        );
        BankAccount to = new DebitAccount(
                "0987654321", "Анна", 1000
        );

        TransferService service = new TransferService(
                new NoCommission(),
                new FakeNotificationService()
        );

        assertThrows(
                TransferLimitExceededException.class,
                () -> service.transfer(from, to, 50001)
        );

        assertEquals(100000, from.getBalance());
        assertEquals(1000, to.getBalance());
    }

    @Test
    void transferWithoutEnoughMoneyThrowsException() {
        BankAccount from = new DebitAccount(
                "1234567890", "Иван", 1000
        );
        BankAccount to = new DebitAccount(
                "0987654321", "Анна", 500
        );

        TransferService service = new TransferService(
                new NoCommission(),
                new FakeNotificationService()
        );

        InsufficientFundsException ex = assertThrows(
                InsufficientFundsException.class,
                () -> service.transfer(from, to, 5000)
        );

        assertEquals("Insufficient funds", ex.getMessage());
        assertEquals(1000, from.getBalance());
        assertEquals(500, to.getBalance());
    }
}