package org.quarkusnatsimplementation;

import io.nats.client.Connection;
import io.nats.client.Dispatcher;
import io.nats.client.Nats;
import io.nats.client.Message;
import io.quarkus.runtime.Startup;
import jakarta.annotation.PostConstruct;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Startup
@Path("/notification")
public class NotificationController {
    @GET
    public String info() {
        return "Notification Service is up and running";
    }

    @PostConstruct
    public void init() {
        try {
            Connection natsConnection = Nats.connect("nats://localhost:4222");
            System.out.println("Notification Service connected to NATS server");

            Dispatcher dispatcher = natsConnection.createDispatcher(this::handleMessage);

            Dispatcher subSuccess = dispatcher.subscribe("order.success", "NotificationQG");
            Dispatcher subFailure = dispatcher.subscribe("order.failure", "NotificationQG");

            System.out.println("Notification Service is listening for status");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleMessage(Message msg) {
        String subject = msg.getSubject();
        String productId = new String(msg.getData());

        if ("order.success".equals(subject)) {
            System.out.println("Order placed successfully for " + productId);
        } else if ("order.failure".equals(subject)) {
            System.out.println("Order did not get placed for " + productId);
        } else {
            System.out.println("Received message on subject " + subject + ": " + productId);
        }
    }


}
