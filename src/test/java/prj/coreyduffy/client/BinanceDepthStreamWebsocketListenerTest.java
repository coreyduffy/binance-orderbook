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
        String jsonInput = """
                {
                  "e": "depthUpdate",
                  "E": 1672515782136,
                  "s": "BNBBTC",
                  "U": 157,
                  "u": 160,
                  "b": [
                    ["0.0024", "10"]
                  ],
                  "a": [
                    ["0.0026", "100"]
                  ]
                }
                """;


        // When
        listener.onText(mockWebSocket, jsonInput, true);

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
