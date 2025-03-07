package org.quarkusnatsimplementation;

import io.nats.client.Connection;
import io.nats.client.Nats;
import io.quarkus.runtime.Startup;
import jakarta.annotation.PostConstruct;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import org.quarkusnatsimplementation.models.Order;

@Startup
@Path("/order")
public class OrderController {
    @GET
    public String info() {
        return "Order Service is up and running";
    }

    private Connection natsConnection;

    @PostConstruct
    public void init() {
        try{
            natsConnection = Nats.connect("nats://localhost:4222");
            System.out.println("Order Service connected to NATS server");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void getOrder(Order order) {
        try {
            natsConnection.publish("order.placed", order.getProductID().getBytes());
            System.out.println("Order service published: " + order.getProductID());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
