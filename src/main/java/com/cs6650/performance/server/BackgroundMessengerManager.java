package com.cs6650.performance.server;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.ws.rs.QueryParam;

/**
 * Created by saikikwok on 19/11/2017.
 */
public class BackgroundMessengerManager implements ServletContextListener {

    private static int THREAD_NUM = 20;
    private static int SQS_MSG_THREAD_NUM = 5;
    private LinkedList<MetricsWorker> threadPool = new LinkedList<>();
    public static ConcurrentLinkedQueue<FailureCountMetrics> failureCounts = new ConcurrentLinkedQueue<>();
    public static ConcurrentLinkedQueue<ResponseTimeMetrics> responseTimes = new ConcurrentLinkedQueue<>();
    public static ConcurrentLinkedQueue<QueryTimeMetrics> queryTimes = new ConcurrentLinkedQueue<>();

    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("messenger threads are requesting...");
        for (int i = 0; i < THREAD_NUM; i++) {
            SQSMesengerProcessor r = new SQSMesengerProcessor();
            threadPool.add(r);
            r.start();
        }
        FailureMetricsWorkder f = new FailureMetricsWorkder();
        threadPool.add(f);
        f.start();
        ResponseTimeMetricsWorker re = new ResponseTimeMetricsWorker();
        threadPool.add(re);
        re.start();
        QueryTimeMetricsWorker q = new QueryTimeMetricsWorker();
        threadPool.add(q);
        q.start();
        System.out.println("messenger threads requesting done...");
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        for (int i = 0; i < THREAD_NUM + SQS_MSG_THREAD_NUM; i++) {
            threadPool.get(i).setDone();
        }
        ConnectionFactory.closeDatasource();
    }


}