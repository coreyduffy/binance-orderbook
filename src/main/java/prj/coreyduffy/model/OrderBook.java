package prj.coreyduffy.model;

import java.util.ArrayList;
import java.util.List;

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
}
