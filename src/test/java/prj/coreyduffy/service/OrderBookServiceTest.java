package prj.coreyduffy.service;

import org.junit.jupiter.api.Test;
import prj.coreyduffy.model.OrderBook;

import static org.junit.jupiter.api.Assertions.*;

class OrderBookServiceTest {
    private final OrderBookService service = OrderBookService.getInstance();

    @Test
    void testConvertSnapshotJsonToOrderBook() {
        // Given
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

        // When
        OrderBook orderBook = service.convertSnapshotJsonToOrderBook(jsonSnapshot);

        // Then
        assertNotNull(orderBook);
        assertEquals(1027024L, orderBook.getLastUpdateId());
        assertEquals(1, orderBook.getBids().size());
        assertEquals("4.00000000", orderBook.getBids().get(0).price());
        assertEquals("431.00000000", orderBook.getBids().get(0).quantity());
        assertEquals(1, orderBook.getAsks().size());
        assertEquals("4.00000200", orderBook.getAsks().get(0).price());
        assertEquals("12.00000000", orderBook.getAsks().get(0).quantity());
    }

    @Test
    void testConvertSnapshotJsonToOrderBook_MultipleBidsAndAsks() {
        // Given
        String jsonSnapshot = """
                {
                  "lastUpdateId": 1027024,
                  "bids": [
                    ["4.00000000", "431.00000000"],
                    ["4.10000000", "432.00000000"],
                    ["4.20000000", "433.00000000"]
                  ],
                  "asks": [
                    ["4.00000200", "12.00000000"],
                    ["4.10000200", "13.00000000"],
                    ["4.20000200", "14.00000000"]
                  ]
                }
                """;

        // When
        OrderBook orderBook = service.convertSnapshotJsonToOrderBook(jsonSnapshot);

        // Then
        assertNotNull(orderBook);
        assertEquals(1027024L, orderBook.getLastUpdateId());
        assertEquals(3, orderBook.getBids().size());
        assertTrue(orderBook.getBids().stream().anyMatch(order -> order.price().equals("4.00000000") && order.quantity().equals("431.00000000")));
        assertTrue(orderBook.getBids().stream().anyMatch(order -> order.price().equals("4.10000000") && order.quantity().equals("432.00000000")));
        assertTrue(orderBook.getBids().stream().anyMatch(order -> order.price().equals("4.20000000") && order.quantity().equals("433.00000000")));
        assertEquals(3, orderBook.getAsks().size());
        assertTrue(orderBook.getAsks().stream().anyMatch(order -> order.price().equals("4.00000200") && order.quantity().equals("12.00000000")));
        assertTrue(orderBook.getAsks().stream().anyMatch(order -> order.price().equals("4.10000200") && order.quantity().equals("13.00000000")));
        assertTrue(orderBook.getAsks().stream().anyMatch(order -> order.price().equals("4.20000200") && order.quantity().equals("14.00000000")));
    }

    @Test
    void testConvertSnapshotJsonToOrderBook_HandlesInvalidJson() {
        // Given
        String jsonSnapshot = """
                INVALID TEXT HERE
                """;

        // When - Then
        OrderBookServiceException exception = assertThrows(OrderBookServiceException.class, () -> service.convertSnapshotJsonToOrderBook(jsonSnapshot));
        assertEquals("Unable to create OrderBook", exception.getMessage());
    }
}