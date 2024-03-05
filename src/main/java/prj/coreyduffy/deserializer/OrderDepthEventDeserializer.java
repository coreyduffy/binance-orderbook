package prj.coreyduffy.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import prj.coreyduffy.model.Order;
import prj.coreyduffy.model.OrderDepthEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OrderDepthEventDeserializer extends JsonDeserializer<OrderDepthEvent> {

    @Override
    public OrderDepthEvent deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode rootNode = p.getCodec().readTree(p);
        OrderDepthEvent event = new OrderDepthEvent();
        event.setLastUpdateId(rootNode.get("u").asLong());
        event.setFirstUpdateId(rootNode.get("U").asLong());
        event.setBids(convertJsonOrders(rootNode.get("b")));
        event.setAsks(convertJsonOrders(rootNode.get("a")));
        return event;
    }

    private List<Order> convertJsonOrders(JsonNode ordersNode) {
        List<Order> orders = new ArrayList<>();
        if (ordersNode != null) {
            for (JsonNode orderNode : ordersNode) {
                String price = orderNode.get(0).asText();
                String quantity = orderNode.get(1).asText();
                orders.add(new Order(price, quantity));
            }
        }
        return orders;
    }
}
