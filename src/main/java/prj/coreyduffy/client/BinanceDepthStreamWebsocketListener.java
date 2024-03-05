package prj.coreyduffy.client;

import java.net.http.WebSocket;
import java.util.concurrent.CompletionStage;

public class BinanceDepthStreamWebsocketListener implements WebSocket.Listener {

    @Override
    public void onOpen(WebSocket webSocket) {
        WebSocket.Listener.super.onOpen(webSocket);
    }

    @Override
    public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
        // TODO: Add code to handle event
        System.out.println("Received message: " + data);
        webSocket.request(1);
        return null;
    }

    @Override
    public void onError(WebSocket webSocket, Throwable error) {
        System.err.println("WebSocket Error: " + error.getMessage());
        WebSocket.Listener.super.onError(webSocket, error);
    }

}
