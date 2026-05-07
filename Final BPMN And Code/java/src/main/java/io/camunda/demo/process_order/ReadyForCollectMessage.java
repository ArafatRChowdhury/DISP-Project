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
public class ReadyForCollectMessage {
    //Initialization of the Logger
    private final static Logger LOG = LoggerFactory.getLogger(ReadyForCollectMessage.class);
    /*
     * Job worker attached to the intermediate send message event "Send Ticket Request to Ticketing Agent"
     * type = "sendInfo" must match with the Task Definition --> Job Type (from the model)
     */
    @JobWorker(type = "ready-for-collect")
    public void priceReceived(final ActivatedJob job, final JobClient client) {
        //Printing Log
        LOG.info("Processing job: {}", job.getKey());
        LOG.info(" job completed: {}", job.getKey());

        CamundaClient camundaClient = CamundaClient.newClient();
        camundaClient.newPublishMessageCommand()
                .messageName("collection-ready")
                .correlationKey("collection-ready")
                .send()
                .join();

        client.newCompleteCommand(job.getKey())
                .send()
                .join();

    }
}
