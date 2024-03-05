import org.junit.jupiter.api.Test;
import prj.coreyduffy.Main;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {
    private static final String ILLEGAL_ARGUMENT_MESSAGE = "Please ensure that one argument (the symbol you would like to query) is passed";

    @Test
    void testMain_CorrectArgument() {
        // Given
        String[] args = {"BTCUSD"};

        // When - Then
        assertDoesNotThrow(() -> Main.main(args));
    }

    @Test
    void testMain_NoArguments() {
        // Given
        String[] args = {};

        // When - Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> Main.main(args));
        assertEquals(ILLEGAL_ARGUMENT_MESSAGE, exception.getMessage());
    }

    @Test
    void testMain_TooManyArguments() {
        // Given
        String[] args = {"Test", "Test2"};

        // When - Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> Main.main(args));
        assertEquals(ILLEGAL_ARGUMENT_MESSAGE, exception.getMessage());
    }
}
