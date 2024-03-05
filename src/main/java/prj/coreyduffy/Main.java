package prj.coreyduffy;

import prj.coreyduffy.client.BinanceDepthStreamWebsocketClient;

public class Main {
    public static void main(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException("Please ensure that one argument (the symbol you would like to query) is passed");
        }
        BinanceDepthStreamWebsocketClient binanceDepthStreamWebsocketClient = new BinanceDepthStreamWebsocketClient();
        binanceDepthStreamWebsocketClient.connect(args[0]);
    }
}