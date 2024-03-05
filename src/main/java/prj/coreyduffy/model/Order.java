package prj.coreyduffy.model;

public class Order {
    private String price;
    private String quantity;

    public Order() {}

    public Order(String price, String quantity) {
        this.price = price;
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
