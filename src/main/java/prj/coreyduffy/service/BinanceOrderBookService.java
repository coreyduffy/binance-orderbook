package prj.coreyduffy.service;

import prj.coreyduffy.client.BinanceDepthStreamWebsocketClient;
import prj.coreyduffy.client.BinanceHttpApiClient;
import prj.coreyduffy.factory.OrderBookFactory;
import prj.coreyduffy.model.OrderBook;

public class BinanceOrderBookService implements OrderBookService {
    private final OrderDepthEventProcessorService orderDepthEventProcessorService;
    private final BinanceDepthStreamWebsocketClient binanceDepthStreamWebsocketClient;
    private final BinanceHttpApiClient binanceHttpApiClient;

    public BinanceOrderBookService(OrderDepthEventProcessorService orderDepthEventProcessorService,
                                   BinanceDepthStreamWebsocketClient binanceDepthStreamWebsocketClient,
                                   BinanceHttpApiClient binanceHttpApiClient) {
        this.orderDepthEventProcessorService = orderDepthEventProcessorService;
        this.binanceDepthStreamWebsocketClient = binanceDepthStreamWebsocketClient;
        this.binanceHttpApiClient = binanceHttpApiClient;
    }

    @Override
    public void printLiveOrderBookUpdates(String symbol, int numOfOrders) {
        binanceDepthStreamWebsocketClient.connect(symbol).join();
        OrderBook orderBook = getOrderBookFromDepthSnapshot(symbol);
        orderDepthEventProcessorService.processBufferedEvents(orderBook, numOfOrders);
    }

    private OrderBook getOrderBookFromDepthSnapshot(String symbol) {
        String bodyString = binanceHttpApiClient.fetchDepthSnapshot(symbol);
        OrderBookFactory orderBookFactory = OrderBookFactory.getInstance();
        return orderBookFactory.convertSnapshotJsonToOrderBook(bodyString);
    }
}
