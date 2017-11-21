package com.cs6650.performance.server;

import org.json.simple.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

/**
 * Created by saikikwok on 22/10/2017.
 */
public class RecordDAO {

    private static final String INSERT_FAILURE_ST =
            "INSERT INTO failure_count (id, hostname, start_time) VALUES (DEFAULT, ?, ?)";
    private static final String INSERT_RESPONSE_ST =
            "INSERT INTO response_time (id, hostname, method_type, start_time, latency) VALUES (DEFAULT, ?, ?, ?, ?)";
    private static final String INSERT_QUERY_ST =
            "INSERT INTO query_time (id, hostname, method_type, start_time, latency) VALUES (DEFAULT, ?, ?, ?, ?)";


    public Boolean insertFailureCounts(LinkedList<FailureCountMetrics> dataset) {
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            PreparedStatement ps = conn.prepareStatement(INSERT_FAILURE_ST);
            for (int i = 0; i < dataset.size(); i++) {
                FailureCountMetrics data = dataset.get(i);
                ps.setString(1, data.getHostname());
                ps.setLong(2, data.getStarttime());
                ps.addBatch();
            }
            int[] updateCounts = ps.executeBatch();
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public Boolean insertResponseTimes(LinkedList<ResponseTimeMetrics> dataset) {
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            PreparedStatement ps = conn.prepareStatement(INSERT_RESPONSE_ST);
            for (ResponseTimeMetrics data : dataset) {
                ps.setString(1, data.getHostname());
                ps.setString(2, data.getMethodType());
                ps.setLong(3, data.getStarttime());
                ps.setLong(4, data.getResponseTime());
                ps.addBatch();
            }
            int[] updateCounts = ps.executeBatch();
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public Boolean insertQueryTimes(LinkedList<QueryTimeMetrics> dataset) {
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            PreparedStatement ps = conn.prepareStatement(INSERT_QUERY_ST);
            for (int i = 0; i < dataset.size(); i++) {
                QueryTimeMetrics data = dataset.get(i);
                ps.setString(1, data.getHostname());
                ps.setString(2, data.getMethod_type());
                ps.setLong(3, data.getStarttime());
                ps.setLong(4, data.getQueryTime());
                ps.addBatch();
            }
            int[] updateCounts = ps.executeBatch();
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return true;
    }


}
