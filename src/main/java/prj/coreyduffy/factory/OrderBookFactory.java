package prj.coreyduffy.factory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import prj.coreyduffy.model.Order;
import prj.coreyduffy.model.OrderBook;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OrderBookFactory {
    private static final OrderBookFactory INSTANCE = new OrderBookFactory();

    private OrderBookFactory() {
    }

    public static OrderBookFactory getInstance() {
        return INSTANCE;
    }

    public OrderBook convertSnapshotJsonToOrderBook(String jsonString) {
        ObjectMapper mapper = new ObjectMapper();
        OrderBook orderBook = new OrderBook();

        try {
            JsonNode rootNode = mapper.readTree(jsonString);
            orderBook.setLastUpdateId(rootNode.get("lastUpdateId").asLong());

            JsonNode bidsNode = rootNode.get("bids");
            orderBook.setBids(convertJsonOrders(bidsNode));

            JsonNode asksNode = rootNode.get("asks");
            orderBook.setAsks(convertJsonOrders(asksNode));

        } catch (IOException e) {
            throw new OrderBookFactoryException("Unable to create OrderBook", e);
        }

        return orderBook;
    }

    private List<Order> convertJsonOrders(JsonNode ordersNode) {
        List<Order> orders = new ArrayList<>();
        if (ordersNode != null) {
            for (JsonNode orderNode : ordersNode) {
                Order order = new Order(orderNode.get(0).asText(), orderNode.get(1).asText());
                orders.add(order);
            }
        }
        return orders;
    }
}

