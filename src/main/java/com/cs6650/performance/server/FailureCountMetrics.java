package com.cs6650.performance.server;

/**
 * Created by saikikwok on 19/11/2017.
 */
public class FailureCountMetrics {

    private String hostname;
    private Integer count;
    private Long starttime;

    public FailureCountMetrics(String hostname, Integer count, Long starttime) {
        this.hostname = hostname;
        this.count = count;
        this.starttime = starttime;
    }

    public String getHostname() {
        return hostname;
    }

    public Integer getCount() {
        return count;
    }

    public Long getStarttime() {
        return starttime;
    }
}
