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
public class PriceReceivedMessage {
    //Initialization of the Logger
    private final static Logger LOG = LoggerFactory.getLogger(PriceReceivedMessage.class);
    /*
     * Job worker attached to the intermediate send message event "Send Ticket Request to Ticketing Agent"
     * type = "sendInfo" must match with the Task Definition --> Job Type (from the model)
     */
    @JobWorker(type = "price-received")
    public void priceReceived(final ActivatedJob job, final JobClient client) {
        //Printing Log
        LOG.info("Processing job: {}", job.getKey());
        LOG.info(" job completed: {}", job.getKey());
        //Get the process variables
        double price = (double) job.getVariable("price");
        String productName = job.getVariable("productName").toString();
        int quantity = (int) job.getVariable("quantity");

        Map<String, Object> result = new HashMap<>();
        result.put("productName", productName);
        result.put("price", price);
        result.put("quantity", quantity);
        /*
         * Creating a Camunda Client
         * This is responsible for handling  the correlation of the messages
         * Message name is the message name given at the recipient of the message (Global Message Reference if this is for a message start event.
         * In your example this message is passed to the "Receive Ticket Request".
         * Investigate Message --> Global Message Reference & Name
         */

        CamundaClient camundaClient = CamundaClient.newClient();
        camundaClient.newPublishMessageCommand()
                .messageName("priceReceivedID")
                .correlationKey("priceReceivedID")
                .variables(result)
                .send()
                .join();

        client.newCompleteCommand(job.getKey())
                .send()
                .join();

    }
}
