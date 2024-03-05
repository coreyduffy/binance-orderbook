package prj.coreyduffy.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import prj.coreyduffy.model.OrderDepthEvent;

import java.net.http.WebSocket;
import java.util.concurrent.CompletionStage;

public class BinanceDepthStreamWebsocketListener implements WebSocket.Listener {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public void onOpen(WebSocket webSocket) {
        WebSocket.Listener.super.onOpen(webSocket);
    }

    @Override
    public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
        try {
            OrderDepthEvent event = OBJECT_MAPPER.readValue(data.toString(), OrderDepthEvent.class);
            webSocket.request(1);
            return null;
        } catch (JsonProcessingException e) {
            throw new BinanceDepthStreamWebsocketException("Unable to handle order depth event", e);
        }
    }

    @Override
    public void onError(WebSocket webSocket, Throwable error) {
        System.err.println("WebSocket Error: " + error.getMessage());
        WebSocket.Listener.super.onError(webSocket, error);
    }

}
