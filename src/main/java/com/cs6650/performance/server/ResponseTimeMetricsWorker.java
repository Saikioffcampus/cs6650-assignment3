package com.cs6650.performance.server;

import java.util.LinkedList;

/**
 * Created by saikikwok on 19/11/2017.
 */
public class ResponseTimeMetricsWorker extends MetricsWorker {
    @Override
    public void run() {
        while (this.isActive) {
            LinkedList<ResponseTimeMetrics> dataset = gatherDataSet();
            if (dataset.size() > 0) {
                RecordDAO dao = new RecordDAO();
                try {
                    dao.insertResponseTimes(dataset);
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private LinkedList<ResponseTimeMetrics> gatherDataSet() {
        LinkedList<ResponseTimeMetrics> dataset = new LinkedList<>();
        long t = System.currentTimeMillis();
        long end = t + INTERVAL;
        while (System.currentTimeMillis() < end && dataset.size() < BATCH_SIZE) {
            ResponseTimeMetrics data = BackgroundMessengerManager.responseTimes.poll();
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
