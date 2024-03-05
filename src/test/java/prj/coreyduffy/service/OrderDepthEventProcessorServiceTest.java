package prj.coreyduffy.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import prj.coreyduffy.model.OrderDepthEvent;

import java.util.concurrent.ConcurrentLinkedQueue;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderDepthEventProcessorServiceTest {

    private OrderDepthEventProcessorService service;
    private ConcurrentLinkedQueue<OrderDepthEvent> eventQueue;

    @BeforeEach
    void setUp() {
        eventQueue = new ConcurrentLinkedQueue<>();
        service = new OrderDepthEventProcessorService(eventQueue);
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
}
