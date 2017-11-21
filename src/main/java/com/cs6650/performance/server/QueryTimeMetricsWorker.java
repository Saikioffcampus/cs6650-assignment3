package com.cs6650.performance.server;

import java.util.LinkedList;

/**
 * Created by saikikwok on 19/11/2017.
 */
public class QueryTimeMetricsWorker extends MetricsWorker {

    @Override
    public void run() {
        while (this.isActive) {
            LinkedList<QueryTimeMetrics> dataset = gatherDataSet();
            if (dataset.size() > 0) {
                RecordDAO dao = new RecordDAO();
                try {
                    dao.insertQueryTimes(dataset);
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private LinkedList<QueryTimeMetrics> gatherDataSet() {
        LinkedList<QueryTimeMetrics> dataset = new LinkedList<>();
        long t = System.currentTimeMillis();
        long end = t + INTERVAL;
        while (System.currentTimeMillis() < end && dataset.size() < BATCH_SIZE) {
            QueryTimeMetrics data = BackgroundMessengerManager.queryTimes.poll();
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
