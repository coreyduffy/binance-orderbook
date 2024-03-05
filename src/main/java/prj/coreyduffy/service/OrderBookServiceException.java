package prj.coreyduffy.service;

public class OrderBookServiceException extends RuntimeException {
    public OrderBookServiceException(String message, Throwable e) {
        super(message, e);
    }
}
