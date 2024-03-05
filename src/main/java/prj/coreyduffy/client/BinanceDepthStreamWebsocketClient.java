package prj.coreyduffy.client;

import java.net.URI;
import java.net.http.HttpClient;

public class BinanceDepthStreamWebsocketClient {
    private static final String DEPTH_WEBSOCKET_URI_TEMPLATE = "wss://stream.binance.com:9443/ws/%s@depth";

    public void connect(String symbol) {
        String lowercaseSymbol = symbol.toLowerCase();
        try (HttpClient client = HttpClient.newHttpClient()) {
            client.newWebSocketBuilder()
                    .buildAsync(URI.create(String.format(DEPTH_WEBSOCKET_URI_TEMPLATE, lowercaseSymbol)), new BinanceDepthStreamWebsocketListener())
                    .join();
        }
    }

}
