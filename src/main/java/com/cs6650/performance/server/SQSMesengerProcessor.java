package com.cs6650.performance.server;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.AmazonSQSException;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.SendMessageBatchRequest;
import com.amazonaws.services.sqs.model.SendMessageBatchRequestEntry;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * Created by saikikwok on 19/11/2017.
 */
public class SQSMesengerProcessor extends MetricsWorker{


    private static String aws_access_key_id = "";
    private static String aws_secret_access_key = "";
    private final String queueUrl =
            "https://sqs.us-west-2.amazonaws.com/761353588507/cs6650-server-performace-queue";
    private AmazonSQS sqsClient;
    private static final int BATCH_SIZE = 10;


    public SQSMesengerProcessor() {
        this.sqsClient = buildClient();
    }

    public void run() {
        while (this.isActive) {
            List<Message> messages = this.sqsClient.receiveMessage(queueUrl).getMessages();
            List<String> msg = new LinkedList<>();
            if (messages.size() == 0) {
                try {
                    Thread.sleep(1000);
                    continue;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            for (Message m : messages) {
                msg.add(m.getBody());
                this.sqsClient.deleteMessage(queueUrl, m.getReceiptHandle());
            }
            for (String s : msg) {
                String[] parts = s.split(";");
                String hostname = parts[0].split(":")[1].trim();
                if (parts.length == 3) {
                    if (parts[2].contains("fail")) {
                        Long starttime = Long.parseLong(parts[1].split(":")[1].trim());
                        FailureCountMetrics f = new FailureCountMetrics(hostname, 1, starttime);
                        BackgroundMessengerManager.failureCounts.offer(f);
                    }
                } else {
                    String methodType = parts[1].split(":")[1];
                    Long starttime = Long.parseLong(parts[2].split(":")[1].trim());
                    Long thetime = Long.parseLong(parts[3].split(":")[1].trim());
                    if (parts[3].contains("query_time")) {
                        QueryTimeMetrics q = new QueryTimeMetrics(hostname, thetime, methodType, starttime);
                        BackgroundMessengerManager.queryTimes.offer(q);
                    } else {
                        ResponseTimeMetrics r = new ResponseTimeMetrics(hostname, methodType, thetime, starttime);
                        BackgroundMessengerManager.responseTimes.offer(r);
                    }
                }
            }
        }
    }

    private AmazonSQS buildClient() {
        AWSCredentials awsCredentials =
                new BasicAWSCredentials(aws_access_key_id, aws_secret_access_key);
        return AmazonSQSClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withRegion(Regions.US_WEST_2).build();
    }

}
