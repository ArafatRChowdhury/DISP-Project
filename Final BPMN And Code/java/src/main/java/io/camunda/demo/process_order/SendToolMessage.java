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

@Component
public class SendToolMessage {
    //Initialization of the Logger
    private final static Logger LOG = LoggerFactory.getLogger(SendToolMessage.class);
    /*
     * Job worker attached to the intermediate send message event "Send Ticket Request to Ticketing Agent"
     * type = "sendInfo" must match with the Task Definition --> Job Type (from the model)
     */
    @JobWorker(type = "send-tool")
    public void sendToolMessage(final ActivatedJob job, final JobClient client) {
        //Printing Log
        LOG.info("Processing job: {}", job.getKey());
        LOG.info(" job completed: {}", job.getKey());

        String productName = job.getVariable("productName").toString();
        String toolCondition = job.getVariable("toolCondition").toString();


        /*
         * Creating a Camunda Client
         * This is responsible for handling  the correlation of the messages
         * Message name is the message name given at the recipient of the message (Global Message Reference if this is for a message start event.
         * In your example this message is passed to the "Receive Ticket Request".
         * Investigate Message --> Global Message Reference & Name
         */

        Map<String, Object> result = new HashMap<>();
        result.put("productName", productName);
        result.put("toolCondition", toolCondition);

        CamundaClient camundaClient = CamundaClient.newClient();
        camundaClient.newPublishMessageCommand()
                .messageName("toolReceived")
                .correlationKey("toolReceived")
                .variables(result)
                .send()
                .join();
    }
}
