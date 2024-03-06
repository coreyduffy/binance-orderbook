package prj.coreyduffy.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import prj.coreyduffy.model.OrderBook;
import prj.coreyduffy.model.OrderDepthEvent;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class OrderDepthEventProcessorServiceTest {
    private ExecutorService executorService;
    private OrderDepthEventProcessorService service;
    private ConcurrentLinkedQueue<OrderDepthEvent> eventQueue;

    @BeforeEach
    void setUp() {
        executorService = Executors.newSingleThreadExecutor();
        eventQueue = new ConcurrentLinkedQueue<>();
        service = new OrderDepthEventProcessorService(eventQueue, executorService);
    }

    @AfterEach
    void tearDown() {
        executorService.shutdownNow();
    }

    @Test
    void testBufferEvent() {
        // Given
        OrderDepthEvent event1 = new OrderDepthEvent();
        event1.setLastUpdateId(1L);
        OrderDepthEvent event2 = new OrderDepthEvent();
        event2.setLastUpdateId(2L);

        // When
        service.bufferEvent(event1);
        service.bufferEvent(event2);

        // Then
        assertEquals(2, eventQueue.size());
        assertEquals(1L, eventQueue.poll().getLastUpdateId());
        assertEquals(2L, eventQueue.poll().getLastUpdateId());
    }

    @Test
    void testProcessBufferedEvents() throws InterruptedException {
        // Given
        OrderDepthEvent event = new OrderDepthEvent();
        eventQueue.add(event);
        OrderBook orderBook = spy(new OrderBook());
        int numOfOrders = 3;

        // When
        service.processBufferedEvents(orderBook, numOfOrders);

        // Then
        Thread.sleep(100);
        verify(orderBook).updateFromEvent(event);
        verify(orderBook).printTopOrders(numOfOrders);
    }

    @Test
    void testProcessBufferedEvents_DoesNothingIfQueueEmpty() {
        // Given
        eventQueue.clear();
        OrderBook orderBook = spy(new OrderBook());

        // When
        service.processBufferedEvents(orderBook, 3);

        // Then
        verifyNoInteractions(orderBook);
    }
}
