package org.quarkusnatsimplementation.models;

public class Order {
    private String productID;

    public Order() {
    }

    public Order(String productID) {
        this.productID = productID;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }
}
