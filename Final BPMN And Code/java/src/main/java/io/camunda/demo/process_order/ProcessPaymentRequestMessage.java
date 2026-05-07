package io.camunda.demo.process_order;
import io.camunda.client.CamundaClient;
import io.camunda.client.annotation.JobWorker;
import io.camunda.client.api.response.ActivatedJob;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
public class ProcessPaymentRequestMessage {
    //Initialization of the Logger
    private final static Logger LOG = LoggerFactory.getLogger(ProcessPaymentRequestMessage.class);
    /*
     * Job worker attached to the intermediate send message event "Send Ticket Request to Ticketing Agent"
     * type = "sendInfo" must match with the Task Definition --> Job Type (from the model)
     */
    @JobWorker(type = "process-payment-request")
    public void processPaymentRequest(final ActivatedJob job, final JobClient client) {
        //Printing Log
        LOG.info("Processing job: {}", job.getKey());
        LOG.info(" job completed: {}", job.getKey());

        String productName = job.getVariable("productName").toString();
        String paymentMethod = job.getVariable("paymentMethod").toString();
        String cardNumber = job.getVariable("cardNumber").toString();
        String customerId = "1";
        double price = (double) job.getVariable("price");
        int quantity = (int) job.getVariable("quantity");
        String fulfilmentMethod = job.getVariable("fulfilmentMethod").toString();
        /*
         * Creating a Camunda Client
         * This is responsible for handling  the correlation of the messages
         * Message name is the message name given at the recipient of the message (Global Message Reference if this is for a message start event.
         * In your example this message is passed to the "Receive Ticket Request".
         * Investigate Message --> Global Message Reference & Name
         */

        Map<String, Object> result = new HashMap<>();
        result.put("productName", productName);
        result.put("paymentMethod", paymentMethod);
        result.put("cardNumber", cardNumber);
        result.put("customerId", customerId);
        result.put("price", price);
        result.put("fulfilmentMethod", fulfilmentMethod);
        result.put("quantity", quantity);


        CamundaClient camundaClient = CamundaClient.newClient();
        camundaClient.newPublishMessageCommand()
                .messageName("paymentSelected")
                .correlationKey("paymentSelected")
                .variables(result)
                .send()
                .join();

        client.newCompleteCommand(job.getKey())
                .send()
                .join();

    }
}
