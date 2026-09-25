import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TransferServiceTest {

    @Test
    void successfulTransferChangesBothBalances() {
        BankAccount from = new DebitAccount("1", "Иван", 10000);
        BankAccount to = new DebitAccount("2", "Пётр", 2000);
        TransferService service = new TransferService(new NoCommission(), new ConsoleNotificationService());

        boolean result = service.transfer(from, to, 3000);

        assertTrue(result);
        assertEquals(7000, from.getBalance());
        assertEquals(5000, to.getBalance());
    }

    @Test
    void failedTransferDoesNotChangeBalances() {
        BankAccount from = new DebitAccount("1", "Иван", 1000);
        BankAccount to = new DebitAccount("2", "Пётр", 2000);
        TransferService service = new TransferService(new NoCommission(), new ConsoleNotificationService());

        boolean result = service.transfer(from, to, 2000);

        assertFalse(result);
        assertEquals(1000, from.getBalance());
        assertEquals(2000, to.getBalance());
    }

    @Test
    void negativeTransferIsForbidden() {
        BankAccount from = new DebitAccount("1", "Иван", 10000);
        BankAccount to = new DebitAccount("2", "Пётр", 2000);
        TransferService service = new TransferService(new NoCommission(), new ConsoleNotificationService());

        boolean result = service.transfer(from, to, -1000);

        assertFalse(result);
    }

    @Test
    void zeroTransferIsForbidden() {
        BankAccount from = new DebitAccount("1", "Иван", 10000);
        BankAccount to = new DebitAccount("2", "Пётр", 2000);
        TransferService service = new TransferService(new NoCommission(), new ConsoleNotificationService());

        boolean result = service.transfer(from, to, 0);

        assertFalse(result);
    }

    @Test
    void cannotTransferToSameAccount() {
        BankAccount account = new DebitAccount("1", "Иван", 10000);
        TransferService service = new TransferService(new NoCommission(), new ConsoleNotificationService());

        boolean result = service.transfer(account, account, 3000);

        assertFalse(result);
        assertEquals(10000, account.getBalance());
    }

    @Test
    void commissionIsChargedFromSender() {
        BankAccount from = new DebitAccount("1", "Иван", 10000);
        BankAccount to = new DebitAccount("2", "Пётр", 2000);
        TransferService service = new TransferService(new PercentCommission(10), new ConsoleNotificationService());

        boolean result = service.transfer(from, to, 3000);

        assertTrue(result);
        assertEquals(6700, from.getBalance());
    }

    @Test
    void receiverGetsExactlyTransferAmount() {
        BankAccount from = new DebitAccount("1", "Иван", 10000);
        BankAccount to = new DebitAccount("2", "Пётр", 2000);
        TransferService service = new TransferService(new PercentCommission(10), new ConsoleNotificationService());

        service.transfer(from, to, 3000);

        assertEquals(5000, to.getBalance());
    }

    @Test
    void transferFailsWhenMoneyIsInsufficientIncludingCommission() {
        BankAccount from = new DebitAccount("1", "Иван", 3000);
        BankAccount to = new DebitAccount("2", "Пётр", 2000);
        TransferService service = new TransferService(new PercentCommission(10), new ConsoleNotificationService());

        boolean result = service.transfer(from, to, 3000);

        assertFalse(result);
        assertEquals(3000, from.getBalance());
        assertEquals(2000, to.getBalance());
    }
    @Test
    void transferDebitToDebit() {
        BankAccount from = new DebitAccount("1", "Иван", 10000);
        BankAccount to = new DebitAccount("2", "Пётр", 2000);
        TransferService service = new TransferService(new NoCommission(), new ConsoleNotificationService());

        boolean result = service.transfer(from, to, 3000);

        assertTrue(result);
        assertEquals(7000, from.getBalance());
        assertEquals(5000, to.getBalance());
    }

    @Test
    void transferDebitToSavings() {
        BankAccount from = new DebitAccount("1", "Иван", 10000);
        BankAccount to = new SavingsAccount("2", "Пётр", 2000, 1000);
        TransferService service = new TransferService(new NoCommission(), new ConsoleNotificationService());

        boolean result = service.transfer(from, to, 3000);

        assertTrue(result);
        assertEquals(7000, from.getBalance());
        assertEquals(5000, to.getBalance());
    }

    @Test
    void transferCreditToDebit() {
        BankAccount from = new CreditAccount("1", "Иван", 1000, 5000);
        BankAccount to = new DebitAccount("2", "Пётр", 2000);

        TransferService service = new TransferService(new NoCommission(), new ConsoleNotificationService());
        boolean result = service.transfer(from, to, 3000);

        assertTrue(result);
        assertEquals(-2000, from.getBalance());
        assertEquals(5000, to.getBalance());
    }

    @Test
    void transferSavingsToDebit() {
        BankAccount from = new SavingsAccount("1", "Иван", 10000, 1000);
        BankAccount to = new DebitAccount("2", "Пётр", 2000);

        TransferService service = new TransferService(new NoCommission(), new ConsoleNotificationService());
        boolean result = service.transfer(from, to, 3000);

        assertTrue(result);
        assertEquals(7000, from.getBalance());
        assertEquals(5000, to.getBalance());
    }
    @Test
    void successfulTransferSendsOneNotification() {
        BankAccount from = new DebitAccount("1", "Иван", 10000);
        BankAccount to = new DebitAccount("2", "Пётр", 2000);

        FakeNotificationService notificationService = new FakeNotificationService();
        TransferService service = new TransferService(new NoCommission(), notificationService);

        boolean result = service.transfer(from, to, 3000);

        assertTrue(result);
        assertEquals(1, notificationService.getNotificationCount());
    }

    @Test
    void failedTransferDoesNotSendNotification() {
        BankAccount from = new DebitAccount("1", "Иван", 1000);
        BankAccount to = new DebitAccount("2", "Пётр", 2000);

        FakeNotificationService notificationService = new FakeNotificationService();
        TransferService service = new TransferService(new NoCommission(), notificationService);

        boolean result = service.transfer(from, to, 2000);

        assertFalse(result);
        assertEquals(0, notificationService.getNotificationCount());
    }

    @Test
    void notificationMessageIsCorrect() {
        BankAccount from = new DebitAccount("1", "Иван", 10000);
        BankAccount to = new DebitAccount("2", "Пётр", 2000);

        FakeNotificationService notificationService = new FakeNotificationService();
        TransferService service = new TransferService(new NoCommission(), notificationService);

        service.transfer(from, to, 3000);

        assertEquals("Transfer 3000.0", notificationService.getLastMessage());}
}