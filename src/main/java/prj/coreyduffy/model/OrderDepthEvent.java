package prj.coreyduffy.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import prj.coreyduffy.deserializer.OrderDepthEventDeserializer;

import java.util.List;

@JsonDeserialize(using = OrderDepthEventDeserializer.class)
public class OrderDepthEvent {
    private long lastUpdateId;
    private long firstUpdateId;
    private List<Order> bids;
    private List<Order> asks;

    public long getLastUpdateId() {
        return lastUpdateId;
    }

    public void setLastUpdateId(long lastUpdateId) {
        this.lastUpdateId = lastUpdateId;
    }

    public long getFirstUpdateId() {
        return firstUpdateId;
    }

    public void setFirstUpdateId(long firstUpdateId) {
        this.firstUpdateId = firstUpdateId;
    }

    public List<Order> getBids() {
        return bids;
    }

    public void setBids(List<Order> bids) {
        this.bids = bids;
    }

    public List<Order> getAsks() {
        return asks;
    }

    public void setAsks(List<Order> asks) {
        this.asks = asks;
    }
}
