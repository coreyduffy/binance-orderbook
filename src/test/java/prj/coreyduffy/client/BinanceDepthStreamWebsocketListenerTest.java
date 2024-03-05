package prj.coreyduffy.client;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import prj.coreyduffy.model.OrderDepthEvent;
import prj.coreyduffy.service.OrderDepthEventProcessorService;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.net.http.WebSocket;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BinanceDepthStreamWebsocketListenerTest {
    private final PrintStream originalErr = System.err;
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    @Mock
    private WebSocket mockWebSocket;
    @Mock
    private OrderDepthEventProcessorService mockOrderDepthEventProcessorService;

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
        BinanceDepthStreamWebsocketListener listener = new BinanceDepthStreamWebsocketListener(mockOrderDepthEventProcessorService);
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
        verify(mockOrderDepthEventProcessorService).bufferEvent(any(OrderDepthEvent.class));
        verify(mockWebSocket).request(1);
    }

    @Test
    void testOnText_IncompleteMessages() {
        // Given
        WebSocket mockWebSocket = mock(WebSocket.class);
        BinanceDepthStreamWebsocketListener listener = new BinanceDepthStreamWebsocketListener(mockOrderDepthEventProcessorService);
        String jsonInput1 = """
                {
                  "e": "depthUpdate",
                  "E": 1672515782136,
                  "s": "BNBBTC",
                  "U": 157,
                  "u": 160,
                  "b": [
                    ["0.0024", "10"]
                  ],
                """;
        String jsonInput2 = """
                  "a": [
                    ["0.0026", "100"]
                  ]
                }
                """;


        // When
        listener.onText(mockWebSocket, jsonInput1, false);
        listener.onText(mockWebSocket, jsonInput2, true);

        // Then
        verify(mockOrderDepthEventProcessorService).bufferEvent(any(OrderDepthEvent.class));
        verify(mockWebSocket, times(2)).request(1);
    }

    @Test
    void testOnText_ThrowsExceptionOnInvalidJson() {
        // Given
        WebSocket mockWebSocket = mock(WebSocket.class);
        BinanceDepthStreamWebsocketListener listener = new BinanceDepthStreamWebsocketListener(mockOrderDepthEventProcessorService);
        String jsonInput = """
                INVALID JSON
                """;


        // When - Then
        BinanceDepthStreamWebsocketException exception = assertThrows(BinanceDepthStreamWebsocketException.class, () -> listener.onText(mockWebSocket, jsonInput, true));
        assertEquals("Unable to parse order depth event", exception.getMessage());
    }

    @Test
    void testOnError() {
        // Given
        Throwable mockError = new RuntimeException("Test error");
        BinanceDepthStreamWebsocketListener listener = new BinanceDepthStreamWebsocketListener(mockOrderDepthEventProcessorService);

        // When
        listener.onError(mockWebSocket, mockError);

        // Then
        assertTrue(errContent.toString().contains("WebSocket Error: Test error"));
    }
}
