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
public class CheckCategoryWorker {
    private static final Logger LOG = LoggerFactory.getLogger(CheckCategoryWorker.class);

    @JobWorker(type = "check-category")
    public void checkAvailability(final ActivatedJob job, final JobClient client) {
        LOG.info("Processing job: {}", job.getKey());
        LOG.info(" job completed: {}", job.getKey());

        String productName = job.getVariable("productName").toString();
        String customerId = "1";
        String toolCondition = job.getVariable("toolCondition").toString();

        Map<String, Object> result = new HashMap<>();
        result.put("productName", productName);
        result.put("customerId", customerId);
        result.put("toolCondition", toolCondition);

        client.newCompleteCommand(job.getKey())
                .variables(result)
                .send()
                .join();
    }
}
