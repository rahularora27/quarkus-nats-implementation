package org.quarkusnatsimplementation;

import io.nats.client.Connection;
import io.nats.client.Dispatcher;
import io.nats.client.Message;
import io.nats.client.Nats;
import io.quarkus.runtime.Startup;
import jakarta.annotation.PostConstruct;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Startup
@Path("/inventory")
public class InventoryController {
    @GET
    public String info() {
        return "Inventory Service is up and running";
    }

    private Connection natsConnection;

    @PostConstruct
    public void init() {
        try{
            natsConnection = Nats.connect("nats://localhost:4222");
            System.out.println("Inventory Service connected to NATS server");

            Dispatcher dispatcher = natsConnection.createDispatcher(this::handleOrder);
            Dispatcher sub = dispatcher.subscribe("order.placed");
            System.out.println("Inventory Service is listening for orders");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleOrder(Message msg) {
        String productID = new String(msg.getData());

        try {
            natsConnection.publish("order.success", productID.getBytes());
            System.out.println("Publish order success for productID " + productID);
        } catch (Exception e) {
            e.printStackTrace();
            natsConnection.publish("order.failure", productID.getBytes());
            System.out.println("Publish order failure for productID " + productID);
        }
    }
}
