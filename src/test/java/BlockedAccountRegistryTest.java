
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BlockedAccountRegistryTest {

    @Test
    void blockMakesAccountBlocked() {
        BlockedAccountRegistry registry = new BlockedAccountRegistry();
        AccountNumber number = new AccountNumber("0000000001");

        registry.block(number);

        assertTrue(registry.isBlocked(number));
    }

    @Test
    void blockingSameAccountTwiceDoesNotCreateDuplicate() {
        BlockedAccountRegistry registry = new BlockedAccountRegistry();
        AccountNumber number = new AccountNumber("0000000001");

        registry.block(number);
        registry.block(number);

        assertEquals(1, registry.size());
    }

    @Test
    void unblockRemovesAccount() {
        BlockedAccountRegistry registry = new BlockedAccountRegistry();
        AccountNumber number = new AccountNumber("0000000001");
        registry.block(number);

        registry.unblock(number);

        assertFalse(registry.isBlocked(number));
        assertEquals(0, registry.size());
    }

    @Test
    void unknownAccountIsNotBlocked() {
        BlockedAccountRegistry registry = new BlockedAccountRegistry();

        assertFalse(registry.isBlocked(
                new AccountNumber("0000000001")
        ));
    }
}
