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
public class ValidateCardWorker {
    private static final Logger LOGGER = LoggerFactory.getLogger(ValidateCardWorker.class);

    @JobWorker(type = "validate-card")
    public void validateCard(final ActivatedJob job, final JobClient client) {
        String tradeCardNumber = job.getVariable("tradeCardNumber").toString();

        Map<String, Object> result = new HashMap<>();
        result.put("tradeCardNumber", tradeCardNumber);

        client.newCompleteCommand(job.getKey())
                .variables(result)
                .send()
                .join();
    }
}
