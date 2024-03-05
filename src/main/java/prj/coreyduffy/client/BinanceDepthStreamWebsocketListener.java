package prj.coreyduffy.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import prj.coreyduffy.model.OrderDepthEvent;
import prj.coreyduffy.service.OrderDepthEventProcessorService;

import java.net.http.WebSocket;
import java.util.concurrent.CompletionStage;

public class BinanceDepthStreamWebsocketListener implements WebSocket.Listener {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final StringBuilder dataBuffer = new StringBuilder();

    private final OrderDepthEventProcessorService orderDepthEventProcessorService;

    public BinanceDepthStreamWebsocketListener(OrderDepthEventProcessorService orderDepthEventProcessorService) {
        this.orderDepthEventProcessorService = orderDepthEventProcessorService;
    }

    @Override
    public void onOpen(WebSocket webSocket) {
        WebSocket.Listener.super.onOpen(webSocket);
    }

    @Override
    public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
        dataBuffer.append(data);

        if (last) {
            try {
                String completeData = dataBuffer.toString();
                OrderDepthEvent event = OBJECT_MAPPER.readValue(completeData, OrderDepthEvent.class);
                orderDepthEventProcessorService.bufferEvent(event);
            } catch (JsonProcessingException e) {
                throw new BinanceDepthStreamWebsocketException("Unable to parse order depth event", e);
            } finally {
                dataBuffer.setLength(0);
            }
        }

        webSocket.request(1);
        return null;
    }

    @Override
    public void onError(WebSocket webSocket, Throwable error) {
        System.err.println("WebSocket Error: " + error.getMessage());
        WebSocket.Listener.super.onError(webSocket, error);
    }

}
