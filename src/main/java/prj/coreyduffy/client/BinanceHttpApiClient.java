package prj.coreyduffy.client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class BinanceHttpApiClient {
    private static final String DEPTH_SNAPSHOT_URL_TEMPLATE = "https://api.binance.com/api/v3/depth?symbol=%s";
    private final HttpClient client;

    public BinanceHttpApiClient(HttpClient client) {
        this.client = client;
    }

    public String fetchDepthSnapshot(String symbol) {
        String uppercaseSymbol = symbol.toUpperCase();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format(DEPTH_SNAPSHOT_URL_TEMPLATE, uppercaseSymbol)))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new BinanceApiClientException("Unable to fetch depth snapshot for symbol: " + uppercaseSymbol, e);
        }
    }
}
