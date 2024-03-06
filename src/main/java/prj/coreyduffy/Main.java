package prj.coreyduffy;

import prj.coreyduffy.client.BinanceDepthStreamWebsocketClient;
import prj.coreyduffy.client.BinanceHttpApiClient;
import prj.coreyduffy.model.OrderDepthEvent;
import prj.coreyduffy.service.BinanceOrderBookService;
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
        ConcurrentLinkedQueue<OrderDepthEvent> eventQueue = new ConcurrentLinkedQueue<>();
        ExecutorService executorService = Executors.newCachedThreadPool();
        OrderDepthEventProcessorService orderDepthEventProcessorService = new OrderDepthEventProcessorService(eventQueue, executorService);
        try (HttpClient httpClient = HttpClient.newHttpClient()) {
            BinanceDepthStreamWebsocketClient binanceDepthStreamWebsocketClient = new BinanceDepthStreamWebsocketClient(httpClient, executorService, orderDepthEventProcessorService);
            BinanceHttpApiClient binanceHttpApiClient = new BinanceHttpApiClient(httpClient);
            OrderBookService orderBookService = new BinanceOrderBookService(orderDepthEventProcessorService, binanceDepthStreamWebsocketClient, binanceHttpApiClient);
            orderBookService.printLiveOrderBookUpdates(symbol, 3);
        }
    }
}