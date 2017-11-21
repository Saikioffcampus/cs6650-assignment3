package com.cs6650.performance.server;

/**
 * Created by saikikwok on 19/11/2017.
 */
public class QueryTimeMetrics {

    private String hostname;
    private Long queryTime;
    private String method_type;
    private Long starttime;

    public QueryTimeMetrics(String hostname, Long queryTime, String method_type, Long starttime) {
        this.hostname = hostname;
        this.method_type = method_type;
        this.queryTime = queryTime;
        this.starttime = starttime;
    }

    public String getHostname() {
        return hostname;
    }

    public Long getQueryTime() {
        return queryTime;
    }

    public String getMethod_type() {
        return method_type;
    }

    public Long getStarttime() {
        return starttime;
    }
}
