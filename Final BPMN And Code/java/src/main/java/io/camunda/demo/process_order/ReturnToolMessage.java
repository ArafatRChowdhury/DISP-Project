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
public class ReturnToolMessage {
    //Initialization of the Logger
    private final static Logger LOG = LoggerFactory.getLogger(ReturnToolMessage.class);
    /*
     * Job worker attached to the intermediate send message event "Send Ticket Request to Ticketing Agent"
     * type = "sendInfo" must match with the Task Definition --> Job Type (from the model)
     */
    @JobWorker(type = "return-tool")
    public void calculatePriceRequest(final ActivatedJob job, final JobClient client) {
        //Printing Log
        LOG.info("Processing job: {}", job.getKey());
        LOG.info(" job completed: {}", job.getKey());

        String toolCondition = job.getVariable("toolCondition").toString();
        String productName = job.getVariable("productName").toString();

        Map<String, Object> result = new HashMap<>();
        result.put("productName", productName);
        result.put("toolCondition", toolCondition);

        CamundaClient camundaClient = CamundaClient.newClient();
        camundaClient.newPublishMessageCommand()
                .messageName("toolReturned")
                .correlationKey("toolReturned")
                .variables(result)
                .send()
                .join();

    }
}
