package prj.coreyduffy.client;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.net.http.WebSocket;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class BinanceDepthStreamWebsocketListenerTest {
    private final PrintStream originalErr = System.err;
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final WebSocket mockWebSocket = mock(WebSocket.class);

    @BeforeEach
    void setUp() {
        System.setErr(new PrintStream(errContent));
    }

    @AfterEach
    void tearDown() {
        System.setErr(originalErr);
    }

    @Test
    void testOnText() {
        // Given
        WebSocket mockWebSocket = mock(WebSocket.class);
        BinanceDepthStreamWebsocketListener listener = new BinanceDepthStreamWebsocketListener();

        // When
        listener.onText(mockWebSocket, "Test Data", true);

        // Then
        verify(mockWebSocket).request(1);
    }

    @Test
    void testOnError() {
        // Given
        Throwable mockError = new RuntimeException("Test error");
        BinanceDepthStreamWebsocketListener listener = new BinanceDepthStreamWebsocketListener();

        // When
        listener.onError(mockWebSocket, mockError);

        // Then
        assertTrue(errContent.toString().contains("WebSocket Error: Test error"));
    }
}
