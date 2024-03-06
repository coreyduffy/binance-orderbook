package prj.coreyduffy.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderBookTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private OrderBook orderBook;

    @BeforeEach
    void setUp() {
        orderBook = new OrderBook();
        orderBook.setLastUpdateId(100);
        orderBook.addBid(new Order("1.0", "100"));
        orderBook.addAsk(new Order("1.2", "200"));
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void testUpdateFromEvent() {
        // Given
        OrderDepthEvent event = new OrderDepthEvent();
        event.setLastUpdateId(101); // Higher than initial lastUpdateId in orderBook
        event.setBids(List.of(new Order("1.1", "150"))); // New bid
        event.setAsks(List.of(new Order("1.2", "0"))); // Remove ask

        // When
        orderBook.updateFromEvent(event);

        // Then
        assertEquals(101, orderBook.getLastUpdateId());
        assertEquals(2, orderBook.getBids().size());
        assertTrue(orderBook.getBids().stream().anyMatch(o -> o.getPrice().equals("1.1") && o.getQuantity().equals("150")));
        assertTrue(orderBook.getAsks().isEmpty());
    }

    @Test
    void testUpdateFromEvent_WithLowerLastUpdateId() {
        // Given
        OrderDepthEvent event = new OrderDepthEvent();
        event.setLastUpdateId(90);
        event.setBids(List.of(new Order("1.05", "100")));
        event.setAsks(List.of(new Order("1.25", "300")));

        // When
        orderBook.updateFromEvent(event);

        // Then
        assertEquals(100, orderBook.getLastUpdateId());
        assertEquals(1, orderBook.getBids().size());
        assertEquals(1, orderBook.getAsks().size());
    }

    @Test
    void testUpdateFromEvent_WithEqualLastUpdateId() {
        // Given
        OrderDepthEvent event = new OrderDepthEvent();
        event.setLastUpdateId(100);
        event.setBids(List.of(new Order("1.05", "100")));
        event.setAsks(List.of(new Order("1.25", "300")));

        // When
        orderBook.updateFromEvent(event);

        // Then
        assertEquals(100, orderBook.getLastUpdateId());
        assertEquals(1, orderBook.getBids().size());
        assertEquals(1, orderBook.getAsks().size());
    }

    @Test
    void testPrintTopOrders() {
        // Given
        OrderBook orderBook = new OrderBook();
        orderBook.addBid(new Order("100.5", "2"));
        orderBook.addBid(new Order("101.5", "1"));
        orderBook.addAsk(new Order("102.5", "1"));
        orderBook.addAsk(new Order("103.5", "3"));
        int numOfOrdersToReturn = 2;

        // When
        orderBook.printTopOrders(numOfOrdersToReturn);

        // Then
        String expectedOutput = "{\"lastUpdateId\":0, \"bids\":[[\"101.5\",\"1\"],[\"100.5\",\"2\"]], \"asks\":[[\"102.5\",\"1\"],[\"103.5\",\"3\"]]}";
        assertTrue(outContent.toString().contains(expectedOutput));
    }

    @Test
    void testPrintTopOrders_HigherNumberToReturnThanOrdersInBook() {
        // Given
        OrderBook orderBook = new OrderBook();
        orderBook.addBid(new Order("100.5", "2"));
        orderBook.addBid(new Order("101.5", "1"));
        orderBook.addAsk(new Order("102.5", "1"));
        orderBook.addAsk(new Order("103.5", "3"));
        int numOfOrdersToReturn = 3;

        // When
        orderBook.printTopOrders(numOfOrdersToReturn);

        // Then
        String expectedOutput = "{\"lastUpdateId\":0, \"bids\":[[\"101.5\",\"1\"],[\"100.5\",\"2\"]], \"asks\":[[\"102.5\",\"1\"],[\"103.5\",\"3\"]]}";
        assertTrue(outContent.toString().contains(expectedOutput));
    }
}
