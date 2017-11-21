package com.cs6650.performance.server;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by saikikwok on 22/10/2017.
 */
public class ConnectionFactory {

    private static final String URL =
            "jdbc:postgresql://cs6650.c5yqzec6hhrx.us-west-2.rds.amazonaws.com/assignment2";
    private static final String USER = "";
    private static final String PASS = "";
    private static DataSource DATA_SOURCE;

    static {
        PoolProperties p = new PoolProperties();
        p.setUrl(URL);
        p.setDriverClassName("org.postgresql.Driver");
        p.setUsername(USER);
        p.setPassword(PASS);
        p.setJmxEnabled(true);
        p.setTestWhileIdle(false);
        p.setTestOnBorrow(true);
        p.setTestOnReturn(false);
        p.setValidationQuery("SELECT 1");
        p.setValidationInterval(30000);
        p.setTimeBetweenEvictionRunsMillis(30000);
        p.setMinEvictableIdleTimeMillis(30000);
        p.setMaxActive(20);
        p.setInitialSize(5);
        p.setMaxWait(20000);
        p.setRemoveAbandonedTimeout(60);
        p.setMaxIdle(20);
        p.setMinIdle(10);
        p.setLogAbandoned(true);
        p.setRemoveAbandoned(true);
        p.setJdbcInterceptors("org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"+
                "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");
        DATA_SOURCE = new DataSource();
        DATA_SOURCE.setPoolProperties(p);
    }

    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = DATA_SOURCE.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static void closeDatasource() {
        DATA_SOURCE.close();
    }


}
