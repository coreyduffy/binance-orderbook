package prj.coreyduffy.service;

import prj.coreyduffy.model.OrderDepthEvent;

import java.util.concurrent.ConcurrentLinkedQueue;

public class OrderDepthEventProcessorService {
    private final ConcurrentLinkedQueue<OrderDepthEvent> eventQueue;

    public OrderDepthEventProcessorService(ConcurrentLinkedQueue<OrderDepthEvent> eventQueue) {
        this.eventQueue = eventQueue;
    }

    public void bufferEvent(OrderDepthEvent event) {
        eventQueue.add(event);
    }
}
