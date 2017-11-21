package com.cs6650.performance.server;

/**
 * Created by saikikwok on 19/11/2017.
 */
public abstract class MetricsWorker extends Thread {

    protected Boolean isActive = true;
    protected Integer BATCH_SIZE = 20;
    protected Integer INTERVAL = 1000;

    public void setDone() {
        this.isActive = false;
    }
}
