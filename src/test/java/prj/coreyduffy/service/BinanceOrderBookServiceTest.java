package prj.coreyduffy.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import prj.coreyduffy.client.BinanceDepthStreamWebsocketClient;
import prj.coreyduffy.client.BinanceHttpApiClient;
import prj.coreyduffy.model.OrderBook;

import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BinanceOrderBookServiceTest {
    @Mock
    private OrderDepthEventProcessorService orderDepthEventProcessorService;
    @Mock
    private BinanceDepthStreamWebsocketClient binanceDepthStreamWebsocketClient;
    @Mock
    private BinanceHttpApiClient binanceHttpApiClient;

    @InjectMocks
    private BinanceOrderBookService service;

    @Test
    void testPrintLiveOrderBookUpdates() {
        // Given
        String symbol = "BTCUSDT";
        int numOfOrders = 3;
        String jsonSnapshot = """
                {
                  "lastUpdateId": 1027024,
                  "bids": [
                    ["4.00000000", "431.00000000"]
                  ],
                  "asks": [
                    ["4.00000200", "12.00000000"]
                  ]
                }
                """;
        when(binanceDepthStreamWebsocketClient.connect(symbol)).thenReturn(CompletableFuture.completedFuture(null));
        when(binanceHttpApiClient.fetchDepthSnapshot(symbol)).thenReturn(jsonSnapshot);

        // When
        service.printLiveOrderBookUpdates(symbol, numOfOrders);

        // Then
        verify(binanceDepthStreamWebsocketClient).connect(symbol);
        verify(binanceHttpApiClient).fetchDepthSnapshot(symbol);
        verify(orderDepthEventProcessorService).processBufferedEvents(any(OrderBook.class), eq(numOfOrders));
    }
}
