package com.cs6650.performance.server;

import java.util.LinkedList;

/**
 * Created by saikikwok on 19/11/2017.
 */
public class FailureMetricsWorkder extends MetricsWorker {

    @Override
    public void run() {
        while (this.isActive) {
            LinkedList<FailureCountMetrics> dataset = gatherDataSet();
            if (dataset.size() > 0) {
                RecordDAO dao = new RecordDAO();
                try {
                    dao.insertFailureCounts(dataset);
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private LinkedList<FailureCountMetrics> gatherDataSet() {
        LinkedList<FailureCountMetrics> dataset = new LinkedList<>();
        long t = System.currentTimeMillis();
        long end = t + INTERVAL;
        while (System.currentTimeMillis() < end && dataset.size() < BATCH_SIZE) {
            FailureCountMetrics data = BackgroundMessengerManager.failureCounts.poll();
            if (data == null) {
                try {
                    Thread.sleep(INTERVAL);
                    continue;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            dataset.add(data);
        }
        return dataset;
    }
}
