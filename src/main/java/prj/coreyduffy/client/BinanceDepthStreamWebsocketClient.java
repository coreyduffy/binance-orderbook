package prj.coreyduffy.client;

import prj.coreyduffy.service.OrderDepthEventProcessorService;

import java.net.URI;
import java.net.http.HttpClient;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public class BinanceDepthStreamWebsocketClient {
    private static final String DEPTH_WEBSOCKET_URI_TEMPLATE = "wss://stream.binance.com:9443/ws/%s@depth";
    private final HttpClient httpClient;
    private final ExecutorService executorService;
    private final OrderDepthEventProcessorService orderDepthEventProcessorService;

    public BinanceDepthStreamWebsocketClient(HttpClient httpClient, ExecutorService executorService, OrderDepthEventProcessorService orderDepthEventProcessorService) {
        this.httpClient = httpClient;
        this.executorService = executorService;
        this.orderDepthEventProcessorService = orderDepthEventProcessorService;
    }

    public CompletableFuture<Void> connect(String symbol) {
        String lowercaseSymbol = symbol.toLowerCase();
        return CompletableFuture.runAsync(() -> httpClient.newWebSocketBuilder()
                .buildAsync(URI.create(String.format(DEPTH_WEBSOCKET_URI_TEMPLATE, lowercaseSymbol)), new BinanceDepthStreamWebsocketListener(orderDepthEventProcessorService))
                .join(), executorService);
    }
}

