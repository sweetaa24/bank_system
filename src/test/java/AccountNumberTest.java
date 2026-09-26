import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AccountNumberTest {

    @Test
    void validNumberIsCreated() {
        AccountNumber number = new AccountNumber("1234567890");

        assertEquals("1234567890", number.value());
    }

    @Test
    void shortNumberIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> new AccountNumber("123"));
    }

    @Test
    void nullNumberIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> new AccountNumber(null));
    }

    @Test
    void emptyNumberIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> new AccountNumber(""));
    }

    @Test
    void nineDigitsAreRejected() {
        assertThrows(IllegalArgumentException.class, () -> new AccountNumber("123456789"));
    }

    @Test
    void elevenDigitsAreRejected() {
        assertThrows(IllegalArgumentException.class, () -> new AccountNumber("12345678901"));
    }

    @Test
    void lettersInsideNumberAreRejected() {
        assertThrows(IllegalArgumentException.class, () -> new AccountNumber("12345A7890"));
    }
}