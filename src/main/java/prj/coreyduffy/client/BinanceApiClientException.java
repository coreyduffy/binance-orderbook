package prj.coreyduffy.client;

public class BinanceApiClientException extends RuntimeException {

    public BinanceApiClientException(String message, Throwable e) {
        super(message, e);
    }
}
