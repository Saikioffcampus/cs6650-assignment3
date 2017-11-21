package com.cs6650.performance.client;

import com.cs6650.performance.server.ConnectionFactory;
import com.cs6650.performance.server.FailureCountMetrics;
import com.cs6650.performance.server.QueryTimeMetrics;
import com.cs6650.performance.server.ResponseTimeMetrics;

import org.json.simple.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by saikikwok on 19/11/2017.
 */
public class MetricsDao {

    private static String retrieve_sql_failure =
            "SELECT hostname, start_time FROM failure_count ORDER BY start_time";
    private static String delete_sql_failure = "DELETE FROM failure_count";
    private static String retrieve_sql_response =
            "SELECT hostname, method_type, start_time, latency FROM response_time ORDER BY start_time";
    private static String delete_sql_response = "DELETE FROM response_time";
    private static String retrieve_sql_query =
            "SELECT hostname, method_type, start_time, latency FROM query_time ORDER BY start_time";
    private static String delete_sql_query = "DELETE FROM query_time";

    public List<FailureCountMetrics> getFailureCounts(String hostname) throws SQLException {
        List<FailureCountMetrics> ret = new LinkedList<>();
        Connection conn = null;
        try{
            conn = ConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(retrieve_sql_failure);
//            ps.setString(1, hostname);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                FailureCountMetrics a =
                        new FailureCountMetrics(resultSet.getString(1), 1, resultSet.getLong(2));
                ret.add(a);
            }
        } finally {
            if (conn != null) conn.close();
        }
        return ret;
    }

    public List<ResponseTimeMetrics> getResponseTimeMetrics(String hostname) throws SQLException {
        List<ResponseTimeMetrics> ret = new LinkedList<>();
        Connection conn = null;
        try{
            conn = ConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(retrieve_sql_response);
//            ps.setString(1, hostname);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                ResponseTimeMetrics a =
                        new ResponseTimeMetrics(
                                resultSet.getString(1),
                                resultSet.getString(2),
                                resultSet.getLong(4),
                                resultSet.getLong(3));
                ret.add(a);
            }
        } finally {
            if (conn != null) conn.close();
        }
        return ret;
    }

    public List<QueryTimeMetrics> getQueryTimeMetrics(String hostname) throws SQLException {
        List<QueryTimeMetrics> ret = new LinkedList<>();
        Connection conn = null;
        try{
            conn = ConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(retrieve_sql_query);
//            ps.setString(1, hostname);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                QueryTimeMetrics a =
                        new QueryTimeMetrics(
                                resultSet.getString(1),
                                resultSet.getLong(4),
                                resultSet.getString(2),
                                resultSet.getLong(3));
                ret.add(a);
            }
        } finally {
            if (conn != null) conn.close();
        }
        return ret;
    }

}
