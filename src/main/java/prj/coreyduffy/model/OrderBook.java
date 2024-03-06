package prj.coreyduffy.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class OrderBook {

    private long lastUpdateId;

    private List<Order> bids = new ArrayList<>();

    private List<Order> asks = new ArrayList<>();

    public long getLastUpdateId() {
        return lastUpdateId;
    }

    public void setLastUpdateId(long lastUpdateId) {
        this.lastUpdateId = lastUpdateId;
    }

    public List<Order> getBids() {
        return bids;
    }

    public void setBids(List<Order> bids) {
        this.bids = bids;
    }

    public void addBid(Order bid) {
        this.bids.add(bid);
    }

    public List<Order> getAsks() {
        return asks;
    }

    public void setAsks(List<Order> asks) {
        this.asks = asks;
    }

    public void addAsk(Order ask) {
        this.asks.add(ask);
    }

    public void updateFromEvent(OrderDepthEvent event) {
        if (event.getLastUpdateId() > getLastUpdateId()) {
            setLastUpdateId(event.getLastUpdateId());
            updateOrders(event.getBids(), getBids());
            updateOrders(event.getAsks(), getAsks());
        }
    }

    public void printTopOrders(int numOfOrders) {
        System.out.println(formatOrderBookJson(getBids(), getAsks(), numOfOrders));
    }

    private String formatOrderBookJson(List<Order> bids, List<Order> asks, int numOfOrders) {
        bids.sort(Comparator.comparingDouble(order -> Double.parseDouble(((Order)order).price())).reversed());
        asks.sort(Comparator.comparingDouble(order -> Double.parseDouble(order.price())));

        List<Order> topBids = bids.stream().limit(numOfOrders).toList();
        List<Order> topAsks = asks.stream().limit(numOfOrders).toList();
        return "{\"lastUpdateId\":" + this.lastUpdateId + ", \"bids\":" + formatOrders(topBids) + ", \"asks\":" + formatOrders(topAsks) + "}";
    }

    private String formatOrders(List<Order> orders) {
        return orders.stream()
                .map(order -> "[\"" + order.price() + "\",\"" + order.quantity() + "\"]")
                .collect(Collectors.joining(",", "[", "]"));
    }

    private void updateOrders(List<Order> newOrders, List<Order> currentOrders) {
        newOrders.forEach(newOrder -> {
            currentOrders.removeIf(order -> order.price().equals(newOrder.price()));
            if (Double.parseDouble(newOrder.quantity()) > 0) {
                currentOrders.add(newOrder);
            }
        });
    }
}
