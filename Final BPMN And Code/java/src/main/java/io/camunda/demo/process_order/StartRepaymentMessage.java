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
public class StartRepaymentMessage {
    //Initialization of the Logger
    private final static Logger LOG = LoggerFactory.getLogger(StartRepaymentMessage.class);
    /*
     * Job worker attached to the intermediate send message event "Send Ticket Request to Ticketing Agent"
     * type = "sendInfo" must match with the Task Definition --> Job Type (from the model)
     */
    @JobWorker(type = "start-repayment")
    public void startRepaymentMessage(final ActivatedJob job, final JobClient client) {
        //Printing Log
        LOG.info("Processing job: {}", job.getKey());
        LOG.info(" job completed: {}", job.getKey());

        double financeAmount = (double) job.getVariable("financeAmount");
        boolean paymentMade = (boolean) job.getVariable("fintrustPaymentMade");
        String customerId = job.getVariable("customerId").toString();
        String paymentMethod = job.getVariable("paymentMethod").toString();

        Map<String, Object> result = new HashMap<>();
        result.put("financeAmount", financeAmount);
        result.put("fintrustPaymentMade", paymentMade);
        result.put("customerId", customerId);
        result.put("paymentMethod", paymentMethod);


        /*
         * Creating a Camunda Client
         * This is responsible for handling  the correlation of the messages
         * Message name is the message name given at the recipient of the message (Global Message Reference if this is for a message start event.
         * In your example this message is passed to the "Receive Ticket Request".
         * Investigate Message --> Global Message Reference & Name
         */

        CamundaClient camundaClient = CamundaClient.newClient();
        camundaClient.newPublishMessageCommand()
                .messageName("startRepayment")
                .correlationKey("startRepayment")
                .variables(result)
                .send()
                .join();
    }
}
