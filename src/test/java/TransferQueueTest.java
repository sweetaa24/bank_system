import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TransferQueueTest {

    @Test
    void requestsAreProcessedInFifoOrder() {
        TransferQueue queue = new TransferQueue();

        TransferRequest a = new TransferRequest(
                new AccountNumber("0000000001"),
                new AccountNumber("0000000002"), 100
        );
        TransferRequest b = new TransferRequest(
                new AccountNumber("0000000003"),
                new AccountNumber("0000000004"), 200
        );
        TransferRequest c = new TransferRequest(
                new AccountNumber("0000000005"),
                new AccountNumber("0000000006"), 300
        );

        queue.add(a);
        queue.add(b);
        queue.add(c);

        assertEquals(a, queue.next());
        assertEquals(b, queue.next());
        assertEquals(c, queue.next());
    }

    @Test
    void peekDoesNotRemoveFirstRequest() {
        TransferQueue queue = new TransferQueue();
        TransferRequest request = new TransferRequest(
                new AccountNumber("0000000001"),
                new AccountNumber("0000000002"), 100
        );

        queue.add(request);

        assertEquals(request, queue.peek());
        assertEquals(1, queue.size());
        assertEquals(request, queue.next());
    }

    @Test
    void nextReturnsNullWhenQueueIsEmpty() {
        TransferQueue queue = new TransferQueue();

        assertNull(queue.next());
    }

    @Test
    void peekReturnsNullWhenQueueIsEmpty() {
        TransferQueue queue = new TransferQueue();

        assertNull(queue.peek());
    }

    @Test
    void sizeDecreasesAfterRetrievingRequest() {
        TransferQueue queue = new TransferQueue();
        queue.add(new TransferRequest(
                new AccountNumber("0000000001"),
                new AccountNumber("0000000002"), 100
        ));
        queue.add(new TransferRequest(
                new AccountNumber("0000000003"),
                new AccountNumber("0000000004"), 200
        ));

        assertEquals(2, queue.size());

        queue.next();

        assertEquals(1, queue.size());
    }

    @Test
    void isEmptyReflectsQueueState() {
        TransferQueue queue = new TransferQueue();

        assertTrue(queue.isEmpty());

        queue.add(new TransferRequest(
                new AccountNumber("0000000001"),
                new AccountNumber("0000000002"), 100
        ));

        assertFalse(queue.isEmpty());

        queue.next();

        assertTrue(queue.isEmpty());
    }
}

