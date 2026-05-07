package io.camunda.demo.process_order;

import io.camunda.client.annotation.JobWorker;
import io.camunda.client.api.response.ActivatedJob;
import io.camunda.client.api.worker.JobClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class GenerateReportsWorker {

    private static final Logger LOGGER = LoggerFactory.getLogger(GenerateReportsWorker.class);

    @JobWorker(type = "generate-reports")
    public void generateReports(final ActivatedJob job, final JobClient client) {
        try {
            String productName  = getRequiredVariable(job, "productName");
            String customerId   = getRequiredVariable(job, "customerId");

            // In production this would generate a servicing report for the tool
            // documenting inspection findings, work carried out, and outcome
            String reportId = "REPORT-" + productName + "-" + System.currentTimeMillis();
            LOGGER.info("Service report generated for tool: {}, customer: {}, reportId: {}",
                    productName, customerId, reportId);

            client.newCompleteCommand(job.getKey())
                    .variable("reportId", reportId)
                    .variable("reportGenerated", true)
                    .send()
                    .join();

        } catch (Exception e) {
            LOGGER.error("Generate reports failed for job {}: {}", job.getKey(), e.getMessage());
            client.newFailCommand(job.getKey())
                    .retries(job.getRetries() - 1)
                    .errorMessage(e.getMessage())
                    .send()
                    .join();
        }
    }

    private String getRequiredVariable(ActivatedJob job, String variableName) {
        Object value = job.getVariable(variableName);
        if (value == null) {
            throw new IllegalStateException("Required process variable is missing: " + variableName);
        }
        return value.toString();
    }
}