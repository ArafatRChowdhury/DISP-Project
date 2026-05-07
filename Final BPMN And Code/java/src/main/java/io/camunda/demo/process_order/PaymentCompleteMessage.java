package io.camunda.demo.process_order;
import io.camunda.client.CamundaClient;
import io.camunda.client.annotation.JobWorker;
import io.camunda.client.api.response.ActivatedJob;

import java.util.HashMap;
import java.util.Map;

import io.camunda.client.api.worker.JobClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
/*
This is the implementation of the Send Ticket Request to Ticketing Agent.
 */
@Component
public class PaymentCompleteMessage {
    //Initialization of the Logger
    private final static Logger LOG = LoggerFactory.getLogger(PaymentCompleteMessage.class);
    /*
     * Job worker attached to the intermediate send message event "Send Ticket Request to Ticketing Agent"
     * type = "sendInfo" must match with the Task Definition --> Job Type (from the model)
     */
    @JobWorker(type = "payment-complete")
    public void paymentComplete(final ActivatedJob job, final JobClient client) {
        //Printing Log
        LOG.info("Processing job: {}", job.getKey());
        LOG.info(" job completed: {}", job.getKey());

        String paymentMethod = job.getVariable("paymentMethod").toString();
        String tradeCardNumber = job.getVariable("tradeCardNumber").toString();
        String productName = job.getVariable("productName").toString();
        String customerId = job.getVariable("customerId").toString();
        double price = (double) job.getVariable("price");
        String fulfilmentMethod = job.getVariable("fulfilmentMethod").toString();
        int quantity = (int) job.getVariable("quantity");

        /*
         * Creating a Camunda Client
         * This is responsible for handling  the correlation of the messages
         * Message name is the message name given at the recipient of the message (Global Message Reference if this is for a message start event.
         * In your example this message is passed to the "Receive Ticket Request".
         * Investigate Message --> Global Message Reference & Name
         */

        Map<String, Object> result = new HashMap<>();
        result.put("paymentMethod", paymentMethod);
        result.put("tradeCardNumber", tradeCardNumber);
        result.put("productName", productName);
        result.put("customerId", customerId);
        result.put("price", price);
        result.put("fulfilmentMethod", fulfilmentMethod);
        result.put("quantity", quantity);

        CamundaClient camundaClient = CamundaClient.newClient();
        camundaClient.newPublishMessageCommand()
                .messageName("completedPayment")
                .correlationKey("completedPayment")
                .variables(result)
                .send()
                .join();
    }
}
