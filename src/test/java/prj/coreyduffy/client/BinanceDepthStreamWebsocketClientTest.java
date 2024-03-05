package prj.coreyduffy.client;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class BinanceDepthStreamWebsocketClientTest {
    @Mock
    private HttpClient mockHttpClient;
    @Mock
    private WebSocket.Builder mockWebSocketBuilder;
    private ExecutorService executorService;

    @BeforeEach
    void setUp() {
        reset(mockHttpClient, mockWebSocketBuilder);
        executorService = Executors.newSingleThreadExecutor();
    }

    @AfterEach
    void tearDown() throws InterruptedException {
        executorService.shutdownNow();
        boolean terminated = executorService.awaitTermination(5, TimeUnit.SECONDS);
        if (!terminated) {
            System.err.println("Executor did not terminate in the allotted time.");
        }
    }

    @Test
    void testConnect() throws InterruptedException {
        // Given
        when(mockHttpClient.newWebSocketBuilder()).thenReturn(mockWebSocketBuilder);
        when(mockWebSocketBuilder.buildAsync(any(URI.class), any(WebSocket.Listener.class)))
                .thenReturn(CompletableFuture.completedFuture(mock(WebSocket.class)));
        BinanceDepthStreamWebsocketClient client = new BinanceDepthStreamWebsocketClient(mockHttpClient, executorService);

        // When
        CompletableFuture<Void> result = client.connect("bnbbtc");

        // Then
        result.join();
        verify(mockWebSocketBuilder)
                .buildAsync(eq(URI.create("wss://stream.binance.com:9443/ws/bnbbtc@depth")), any(BinanceDepthStreamWebsocketListener.class));
        executorService.shutdownNow();
    }

    @Test
    void testConnect_lowercasesSymbol() {
        // Given
        when(mockHttpClient.newWebSocketBuilder()).thenReturn(mockWebSocketBuilder);
        when(mockWebSocketBuilder.buildAsync(any(URI.class), any(WebSocket.Listener.class)))
                .thenReturn(CompletableFuture.completedFuture(mock(WebSocket.class)));
        BinanceDepthStreamWebsocketClient client = new BinanceDepthStreamWebsocketClient(mockHttpClient, executorService);

        // When
        CompletableFuture<Void> result = client.connect("BNBBTC");

        // Then
        result.join();
        verify(mockWebSocketBuilder)
                .buildAsync(eq(URI.create("wss://stream.binance.com:9443/ws/bnbbtc@depth")), any(BinanceDepthStreamWebsocketListener.class));
        executorService.shutdownNow();
    }
}
