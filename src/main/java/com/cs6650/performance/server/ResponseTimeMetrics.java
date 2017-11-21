package com.cs6650.performance.server;

/**
 * Created by saikikwok on 19/11/2017.
 */
public class ResponseTimeMetrics {

    private String hostname;
    private String methodType;
    private Long responseTime;
    private Long starttime;

    public ResponseTimeMetrics(String hostname, String methodType, Long responseTime, Long starttime) {
        this.hostname = hostname;
        this.methodType = methodType;
        this.responseTime = responseTime;
        this.starttime = starttime;
    }

    public String getHostname() {
        return hostname;
    }

    public String getMethodType() {
        return methodType;
    }

    public Long getResponseTime() {
        return responseTime;
    }

    public Long getStarttime() {
        return starttime;
    }

    @Override
    public String toString() {
        return "ResponseTimeMetrics{" +
                "hostname='" + hostname + '\'' +
                ", methodType='" + methodType + '\'' +
                ", responseTime=" + responseTime +
                ", starttime=" + starttime +
                '}';
    }
}
