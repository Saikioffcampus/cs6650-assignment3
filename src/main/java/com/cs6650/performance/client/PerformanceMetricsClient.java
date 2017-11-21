package com.cs6650.performance.client;

import com.cs6650.performance.server.QueryTimeMetrics;
import com.cs6650.performance.server.ResponseTimeMetrics;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.RegularTimePeriod;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by saikikwok on 20/11/2017.
 */
public class PerformanceMetricsClient {


  public static void main(String[] argv) throws Exception {
    // Please, do not remove this line from file template, here invocation of web service will be inserted
    System.out.println("Total: ");
    getStatsForResponseTimes("total");
    getStatsForQueryTimes("total");
  }

  private static void getStatsForResponseTimes(String hostname) throws Exception{
    MetricsDao dao = new MetricsDao();
    List<ResponseTimeMetrics> responseTimeMetricses = dao.getResponseTimeMetrics(hostname);
    List<Long> latencies = new LinkedList<>();
    for (ResponseTimeMetrics r : responseTimeMetricses) {
      latencies.add(r.getResponseTime());
    }
    double[] stats = Util.parseLatency(latencies);
    System.out.println("——————————————————————————————");
    System.out.println("Response Times: ");
    System.out.println("Mean latency for all requests: " + stats[Util.MEAN] + " ms");
    System.out.println("Median latency for all requests: " + stats[Util.MEDIAN] + " ms");
    System.out.println("P95 latency for all requests: " + stats[Util.P95] + " ms");
    System.out.println("P99 latency for all requests: " + stats[Util.P99] + " ms");
    System.out.println("——————————————————————————————");
    ClientChartFactory clientChartFactory = new ClientChartFactory();
    clientChartFactory.populateSeries(responseTimeMetricses);
    JFreeChart jFreeChart = clientChartFactory.createChartForResponse("Response Time Chart");
    File file = new File("response_time.jpeg");
    ChartUtilities.saveChartAsJPEG(file, jFreeChart, 500, 270);
  }

  private static void getStatsForQueryTimes(String hostname) throws Exception {
    MetricsDao dao = new MetricsDao();
    List<QueryTimeMetrics> queryTimeMetricses = dao.getQueryTimeMetrics(hostname);
    List<Long> latencies = new LinkedList<>();
    for (QueryTimeMetrics r : queryTimeMetricses) {
      latencies.add(r.getQueryTime());
    }
    double[] stats = Util.parseLatency(latencies);
    System.out.println("——————————————————————————————");
    System.out.println("Query Times: ");
    System.out.println("Mean latency for all requests: " + stats[Util.MEAN] + " ms");
    System.out.println("Median latency for all requests: " + stats[Util.MEDIAN] + " ms");
    System.out.println("P95 latency for all requests: " + stats[Util.P95] + " ms");
    System.out.println("P99 latency for all requests: " + stats[Util.P99] + " ms");
    System.out.println("——————————————————————————————");
    ClientChartFactory clientChartFactory = new ClientChartFactory();
    clientChartFactory.populateSeriesQuery(queryTimeMetricses);
    JFreeChart jFreeChart = clientChartFactory.createChartForQuery("Query Time Chart");
    File file = new File("query_time.jpeg");
    ChartUtilities.saveChartAsJPEG(file, jFreeChart, 500, 270);
  }


}
