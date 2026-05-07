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
public class ProcessNameWorker {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessNameWorker.class);
    @JobWorker(type = "process_name")
    public void processName(final ActivatedJob job, final JobClient client){
        System.out.printf("Job ID : %s%n", job.getKey());
        String firstName = job.getVariable("firstName").toString();
        String lastName = job.getVariable("lastName").toString();
        String fullName = firstName + " " + lastName;
        System.out.println("First Name: " + firstName + " Last Name: " + lastName + " Full Name: " + fullName);
        Map<String, Object> result = new HashMap<>();
        result.put("fullName", fullName);
        System.out.println(">>>" + firstName);
        client.newCompleteCommand(job.getKey())
                .variables(result)
                .send()
                .join();
    }
}
