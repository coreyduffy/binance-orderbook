package prj.coreyduffy.service;

import prj.coreyduffy.model.OrderBook;
import prj.coreyduffy.model.OrderDepthEvent;

import java.util.Queue;
import java.util.concurrent.ExecutorService;

public class OrderDepthEventProcessorService {
    private final Queue<OrderDepthEvent> eventQueue;
    private final ExecutorService executorService;

    public OrderDepthEventProcessorService(Queue<OrderDepthEvent> eventQueue,
                                           ExecutorService executorService) {
        this.eventQueue = eventQueue;
        this.executorService = executorService;
    }

    public void bufferEvent(OrderDepthEvent event) {
        eventQueue.add(event);
    }

    public void processBufferedEvents(OrderBook orderBook, int numOfOrders) {
        executorService.execute(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                OrderDepthEvent orderDepthEvent = null;
                synchronized (eventQueue) {
                    if (!eventQueue.isEmpty()) {
                        orderDepthEvent = eventQueue.poll();
                    }
                }
                if (orderDepthEvent != null) {
                    orderBook.updateFromEvent(orderDepthEvent);
                    orderBook.printTopOrders(numOfOrders);
                } else {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
        });
    }
}
