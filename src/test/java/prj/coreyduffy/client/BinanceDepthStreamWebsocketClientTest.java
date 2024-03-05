package prj.coreyduffy.client;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class BinanceDepthStreamWebsocketClientTest {

    @Test
    void testConnect() {
        // Given
        try (MockedStatic<HttpClient> mockedHttpClient = mockStatic(HttpClient.class)) {
            HttpClient mockHttpClient = mock(HttpClient.class);
            mockedHttpClient.when(HttpClient::newHttpClient).thenReturn(mockHttpClient);
            WebSocket.Builder mockWebSocketBuilder = mock(WebSocket.Builder.class);
            when(mockHttpClient.newWebSocketBuilder()).thenReturn(mockWebSocketBuilder);
            when(mockWebSocketBuilder.buildAsync(any(URI.class), any(WebSocket.Listener.class)))
                    .thenReturn(mock(CompletableFuture.class));
            BinanceDepthStreamWebsocketClient client = new BinanceDepthStreamWebsocketClient();

            // When
            client.connect("bnbbtc");

            // Then
            verify(mockWebSocketBuilder)
                    .buildAsync(eq(URI.create("wss://stream.binance.com:9443/ws/bnbbtc@depth")), any(BinanceDepthStreamWebsocketListener.class));
        }
    }

    @Test
    void testConnect_lowercasesSymbol() {
        // Given
        try (MockedStatic<HttpClient> mockedHttpClient = mockStatic(HttpClient.class)) {
            HttpClient mockHttpClient = mock(HttpClient.class);
            mockedHttpClient.when(HttpClient::newHttpClient).thenReturn(mockHttpClient);
            WebSocket.Builder mockWebSocketBuilder = mock(WebSocket.Builder.class);
            when(mockHttpClient.newWebSocketBuilder()).thenReturn(mockWebSocketBuilder);
            when(mockWebSocketBuilder.buildAsync(any(URI.class), any(WebSocket.Listener.class)))
                    .thenReturn(mock(CompletableFuture.class));
            BinanceDepthStreamWebsocketClient client = new BinanceDepthStreamWebsocketClient();

            // When
            client.connect("BNBBTC");

            // Then
            verify(mockWebSocketBuilder)
                    .buildAsync(eq(URI.create("wss://stream.binance.com:9443/ws/bnbbtc@depth")), any(BinanceDepthStreamWebsocketListener.class));
        }
    }
}
