package io.camunda.demo.process_order;

import io.camunda.client.annotation.JobWorker;
import io.camunda.client.api.response.ActivatedJob;

import io.camunda.client.api.worker.JobClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CheckPaymentMethodWorker {
    private static final Logger LOGGER = LoggerFactory.getLogger(CheckPaymentMethodWorker.class);

    @JobWorker(type = "check-payment-method")
    public void checkAvailability(final ActivatedJob job, final JobClient client) {
        //when a database is implemented, this would check that database for the tool
        //for now just assume that the tool is always available
        String paymentMethod = job.getVariable("paymentMethod").toString();

        client.newCompleteCommand(job.getKey())
                .variable("paymentMethod", paymentMethod)
                .send()
                .join();
    }
}
