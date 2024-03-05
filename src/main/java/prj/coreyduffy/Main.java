package prj.coreyduffy;

import prj.coreyduffy.client.BinanceApiClient;
import prj.coreyduffy.client.BinanceDepthStreamWebsocketClient;
import prj.coreyduffy.model.OrderBook;
import prj.coreyduffy.model.OrderDepthEvent;
import prj.coreyduffy.service.OrderBookService;
import prj.coreyduffy.service.OrderDepthEventProcessorService;

import java.net.http.HttpClient;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException("Please ensure that one argument (the symbol you would like to query) is passed");
        }
        String symbol = args[0];
        ExecutorService executorService = Executors.newCachedThreadPool();
        try (HttpClient httpClient = HttpClient.newHttpClient()) {
            ConcurrentLinkedQueue<OrderDepthEvent> eventQueue = new ConcurrentLinkedQueue<>();
            OrderDepthEventProcessorService orderDepthEventProcessorService = new OrderDepthEventProcessorService(eventQueue);
            BinanceDepthStreamWebsocketClient binanceDepthStreamWebsocketClient = new BinanceDepthStreamWebsocketClient(httpClient, executorService, orderDepthEventProcessorService);
            binanceDepthStreamWebsocketClient.connect(symbol).join();
            BinanceApiClient binanceApiClient = new BinanceApiClient(httpClient);
            String bodyString = binanceApiClient.fetchDepthSnapshot(symbol);
            OrderBookService orderBookService = OrderBookService.getInstance();
            OrderBook orderBook = orderBookService.convertSnapshotJsonToOrderBook(bodyString);
        }
    }
}