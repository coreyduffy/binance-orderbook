package prj.coreyduffy.deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.jupiter.api.Test;
import prj.coreyduffy.model.Order;
import prj.coreyduffy.model.OrderDepthEvent;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class OrderDepthEventDeserializerTest {

    @Test
    void testDeserialize() throws Exception {
        // Given
        String jsonInput = """
                {
                  "e": "depthUpdate",
                  "E": 1672515782136,
                  "s": "BNBBTC",
                  "U": 157,
                  "u": 160,
                  "b": [
                    ["0.0024", "10"]
                  ],
                  "a": [
                    ["0.0026", "100"]
                  ]
                }
                """;
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(OrderDepthEvent.class, new OrderDepthEventDeserializer());
        mapper.registerModule(module);

        // When
        OrderDepthEvent event = mapper.readValue(jsonInput, OrderDepthEvent.class);

        // Then
        assertNotNull(event);
        assertEquals(160, event.getLastUpdateId());
        assertEquals(157, event.getFirstUpdateId());
        assertNotNull(event.getBids());
        assertEquals(1, event.getBids().size());
        Order bid = event.getBids().get(0);
        assertEquals("0.0024", bid.price());
        assertEquals("10", bid.quantity());
        assertNotNull(event.getAsks());
        assertEquals(1, event.getAsks().size());
        Order ask = event.getAsks().get(0);
        assertEquals("0.0026", ask.price());
        assertEquals("100", ask.quantity());
    }

    @Test
    void testDeserialize_WithMalformedJson() {
        // Given
        String malformedJson = "{ This is not JSON }";
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(OrderDepthEvent.class, new OrderDepthEventDeserializer());
        mapper.registerModule(module);

        // When - Then
        assertThrows(IOException.class, () -> {
            mapper.readValue(malformedJson, OrderDepthEvent.class);
        });
    }

}
