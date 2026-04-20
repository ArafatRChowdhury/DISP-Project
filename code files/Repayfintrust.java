package com.example.camundaworker;

import io.camunda.client.annotation.JobWorker;
import io.camunda.client.api.response.ActivatedJob;
import io.camunda.client.api.worker.JobClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class RepayFintrustWorker {

    private static final Logger LOGGER = LoggerFactory.getLogger(RepayFintrustWorker.class);

    @JobWorker(type = "repay-fintrust")
    public void repayFintrust(final ActivatedJob job, final JobClient client) {
        // Retrieve process variables set earlier in the flow
        String customerId     = job.getVariable("customerId").toString();
        double financeAmount  = Double.parseDouble(job.getVariable("financeAmount").toString());
        String paymentMethod  = job.getVariable("paymentMethod").toString();

        LOGGER.info("Processing Fintrust repayment for customer: {}, amount: {}, method: {}",
                customerId, financeAmount, paymentMethod);

        // In a real implementation this would call the Fintrust repayment API/service.
        // For now we simulate a successful repayment.
        boolean repaymentSuccessful = financeAmount > 0;

        Map<String, Object> variables = new HashMap<>();
        variables.put("repaymentSuccessful", repaymentSuccessful);
        variables.put("repaymentAmount", financeAmount);

        LOGGER.info("Fintrust repayment outcome for customer {}: {}",
                customerId, repaymentSuccessful ? "SUCCESS" : "FAILED");

        client.newCompleteCommand(job.getKey())
                .variables(variables)
                .send()
                .join();
    }
}
