package prj.coreyduffy.client;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BinanceHttpApiClientTest {

    @Mock
    private HttpClient mockHttpClient;

    @Mock
    private HttpResponse<String> mockResponse;

    @InjectMocks
    private BinanceHttpApiClient apiClient;

    @Test
    void testFetchDepthSnapshot() throws IOException, InterruptedException {
        // Given
        String expectedResponseBody = "{\"lastUpdateId\": 10}";
        String symbol = "BTCUSDT";
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandlers.ofString().getClass()))).thenReturn(mockResponse);
        when(mockResponse.body()).thenReturn(expectedResponseBody);

        // When
        String result = apiClient.fetchDepthSnapshot(symbol);

        // Then
        verify(mockHttpClient).send(argThat(request -> request.uri().toString().equals("https://api.binance.com/api/v3/depth?symbol=" + symbol)), any());
        assertEquals(expectedResponseBody, result);
    }

    @Test
    void testFetchDepthSnapshot_ConvertsSymbolToUppercase() throws IOException, InterruptedException {
        // Given
        String expectedResponseBody = "{\"lastUpdateId\": 10}";
        String symbol = "btcusdt";
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandlers.ofString().getClass()))).thenReturn(mockResponse);
        when(mockResponse.body()).thenReturn(expectedResponseBody);

        // When
        String result = apiClient.fetchDepthSnapshot(symbol);

        // Then
        verify(mockHttpClient).send(argThat(request -> request.uri().toString().equals("https://api.binance.com/api/v3/depth?symbol=BTCUSDT")), any());
        assertEquals(expectedResponseBody, result);
    }

    @Test
    void testFetchDepthSnapshot_HandlesIOException() throws IOException, InterruptedException {
        // Given
        String symbol = "BTCUSDT";
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandlers.ofString().getClass()))).thenThrow(IOException.class);

        // When - Then
        BinanceApiClientException exception = assertThrows(BinanceApiClientException.class, () -> apiClient.fetchDepthSnapshot(symbol));
        assertEquals("Unable to fetch depth snapshot for symbol: " + symbol, exception.getMessage());
    }

    @Test
    void testFetchDepthSnapshot_HandlesInterruptedException() throws IOException, InterruptedException {
        // Given
        String symbol = "BTCUSDT";
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandlers.ofString().getClass()))).thenThrow(InterruptedException.class);

        // When - Then
        BinanceApiClientException exception = assertThrows(BinanceApiClientException.class, () -> apiClient.fetchDepthSnapshot(symbol));
        assertEquals("Unable to fetch depth snapshot for symbol: " + symbol, exception.getMessage());
    }
}
