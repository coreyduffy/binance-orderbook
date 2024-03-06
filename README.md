# Binance Orderbook

This console application retrieves and displays the most recent Order Book for a specified market symbol (e.g., BTCUSDT) from the Binance Exchange in real-time. 

Utilising both the Binance REST API and WebSocket API, the application fetches an initial Order Book snapshot and subsequently listens for and processes live updates. 

The first 3 orders for both buy (bids) and sell (asks) sides/orders are displayed in JSON format on STDOUT, updating with each new event received. 

The application sorts bids in descending order and asks in ascending order by price, presenting them in a clear and continuously updated manner.

## How To Run The Application

### Via Maven
#### Compiling the application
You can compile the application by running:
```
mvn clean package
```

### Running the application
#### Packaged from Maven
If you've followed the steps above to compile the application via maven, you can navigate to the target folder and run the application via: 
```
cd target
java -jar binance-orderbook-1.0-SNAPSHOT.jar <YOUR SYMBOL HERE>
```

#### Via IntelliJ
Create a new configuration for the Main class like so:
![img.png](img.png)

## How To Run Tests
You can run tests by simply executing the command below, from the root of this project:
```
mvn test
```

or from IntelliJ you can right-click the `src/test/java/prj/coreyduffy` folder and click 'Run Tests in coreyduffy'
![img_1.png](img_1.png)