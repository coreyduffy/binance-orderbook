package prj.coreyduffy;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static prj.coreyduffy.Main.main;

class MainTest {
    private static final String ILLEGAL_ARGUMENT_MESSAGE = "Please ensure that one argument (the symbol you would like to query) is passed";

    @Test
    void testMain_CorrectArgument() {
        // Given
        String[] args = {"BNBBTC"};

        // When - Then
        assertDoesNotThrow(() -> main(args));
    }

    @Test
    void testMain_NoArguments() {
        // Given
        String[] args = {};

        // When - Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> main(args));
        assertEquals(ILLEGAL_ARGUMENT_MESSAGE, exception.getMessage());
    }

    @Test
    void testMain_TooManyArguments() {
        // Given
        String[] args = {"Test", "Test2"};

        // When - Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> main(args));
        assertEquals(ILLEGAL_ARGUMENT_MESSAGE, exception.getMessage());
    }
}
