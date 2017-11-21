package com.cs6650.performance.client;

import com.cs6650.performance.server.QueryTimeMetrics;
import com.cs6650.performance.server.ResponseTimeMetrics;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

import java.awt.*;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.swing.*;

/**
 * Created by saikikwok on 18/11/2017.
 */
public class ClientChartFactory extends JFrame {

    private final TimeSeries series1_r;
    private final TimeSeries series2_r;
    private final TimeSeries series3_r;
    private final TimeSeries series4_r;
    private final TimeSeries series1_q;
    private final TimeSeries series2_q;
    private final TimeSeries series3_q;
    private final TimeSeries series4_q;

    public ClientChartFactory() {
        series1_r = new TimeSeries("p99 - response");
        series2_r = new TimeSeries("p95 - response ");
        series3_r = new TimeSeries("median - response");
        series4_r = new TimeSeries("mean - response");
        series1_q = new TimeSeries("p99 - query");
        series2_q = new TimeSeries("p95 - query");
        series3_q = new TimeSeries("median - query");
        series4_q = new TimeSeries("mean - query");
    }

    private XYDataset createDataset(final TimeSeries series) {
        return new TimeSeriesCollection(series);
    }

    private void meanSeries(final XYPlot plot) {
        final ValueAxis xaxis = plot.getDomainAxis();
        xaxis.setAutoRange(true);
        xaxis.setVerticalTickLabels(true);

        final ValueAxis yaxis = plot.getRangeAxis();
        yaxis.setAutoRange(true);
        final XYItemRenderer renderer = plot.getRenderer();
        renderer.setSeriesPaint(0, Color.RED);

        final NumberAxis yAxis1 = (NumberAxis) plot.getRangeAxis();
        yAxis1.setTickLabelPaint(Color.RED);
    }

    private void otherSeries(final XYPlot plot, TimeSeries series, int index, Color color) {
        final XYDataset secondDataset = this.createDataset(series);
        plot.setDataset(index, secondDataset);
        plot.mapDatasetToDomainAxis(index, 0);
        plot.mapDatasetToRangeAxis(index, 0);

        final XYItemRenderer renderer = new XYLineAndShapeRenderer(true, false);
        renderer.setSeriesPaint(0, color);
        plot.setRenderer(index, renderer);
    }

    public JFreeChart createChartForResponse(String title) {
        return createChart(title, series4_r, series1_r, series2_r, series3_r);
    }

    public JFreeChart createChartForQuery(String title) {
        return createChart(title, series4_q, series1_q, series2_q, series3_q);
    }

    private JFreeChart createChart(String title, TimeSeries series4,
                                                TimeSeries series1,
                                                TimeSeries series2,
                                                TimeSeries series3) {
        final XYDataset dataset = createDataset(series4);
        final JFreeChart result =
                ChartFactory.createTimeSeriesChart(title, "time", "latency", dataset);
        final XYPlot plot = result.getXYPlot();
        plot.setBackgroundPaint(new Color(0xffffe0));
        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);

        this.meanSeries(plot);
        this.otherSeries(plot, series1, 1, Color.BLUE);
        this.otherSeries(plot, series2, 2, Color.GREEN);
        this.otherSeries(plot, series3, 3, Color.YELLOW);
        return result;
    }

    // list should be sorted by starttime
    public void populateSeries(List<ResponseTimeMetrics> datalist) {
        List<Long> latencies = new LinkedList<>();
        for (int i = 0; i < datalist.size(); i++) {
            ResponseTimeMetrics data = datalist.get(i);
            Date date = new Date(data.getStarttime());
            latencies.add(data.getResponseTime());
            if (i != datalist.size() - 1) {
                ResponseTimeMetrics next = datalist.get(i + 1);
                Date dateNext = new Date(next.getStarttime());
                if (dateNext.toString().equals(date.toString())) {
                    continue;
                }
            }
            double[] stats = Util.parseLatency(latencies);
            Millisecond m = new Millisecond(date);
            series4_r.add(m, stats[Util.MEAN]);
            series1_r.add(m, stats[Util.P99]);
            series2_r.add(m, stats[Util.P95]);
            series3_r.add(m, stats[Util.MEDIAN]);
            latencies.clear();
        }
    }

    public void populateSeriesQuery(List<QueryTimeMetrics> datalist) {
        List<Long> latencies = new LinkedList<>();
        for (int i = 0; i < datalist.size(); i++) {
            QueryTimeMetrics data = datalist.get(i);
            Date date = new Date(data.getStarttime());
            latencies.add(data.getQueryTime());
            if (i != datalist.size() - 1) {
                QueryTimeMetrics next = datalist.get(i + 1);
                Date dateNext = new Date(next.getStarttime());
                if (dateNext.toString().equals(date.toString())) {
                    continue;
                }
            }
            double[] stats = Util.parseLatency(latencies);
            Millisecond m = new Millisecond(date);
            series4_q.add(m, stats[Util.MEAN]);
            series1_q.add(m, stats[Util.P99]);
            series2_q.add(m, stats[Util.P95]);
            series3_q.add(m, stats[Util.MEDIAN]);
            latencies.clear();
        }
    }

}
