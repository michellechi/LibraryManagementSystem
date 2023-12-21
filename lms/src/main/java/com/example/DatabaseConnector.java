package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.ResultSetMetaData;

public class DatabaseConnector {
    private Connection connection;
    private Properties properties;

    public DatabaseConnector() {
        loadProperties();
    }
    /* 
    serets.properties should contain the following:
        username=your_username
        password=your_password
    */
    private void loadProperties() {
        this.properties = new Properties();
        System.out.println(System.getProperty("user.dir"));
        try (FileInputStream fis = new FileInputStream("secrets.properties")) {
            properties.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void connect() {
        String username = properties.getProperty("username");
        String password = properties.getProperty("password");

        String jdbcConnectorUrl = "jdbc:mysql://ambari-node5.csc.calpoly.edu:3306" + "/" + username + "?allowMultiQueries=true";

        try {
            DriverManager.setLoginTimeout(5);
            connection = DriverManager.getConnection(jdbcConnectorUrl, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Map<String, Object>> runParametrizedQuery(String query, Object... params) {
        List<Map<String, Object>> results = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            setParameters(stmt, params);
            boolean isResultSet = stmt.execute();
            
            if (isResultSet) {
                try (ResultSet resultSet = stmt.getResultSet()) {
                    ResultSetMetaData rsmd = resultSet.getMetaData();
                    int columnsNumber = rsmd.getColumnCount();

                    while (resultSet.next()) {
                        Map<String, Object> row = new HashMap<>();
                        for (int i = 1; i <= columnsNumber; i++) {
                            String columnName = rsmd.getColumnLabel(i);
                            Object columnValue = resultSet.getObject(i);
                            row.put(columnName, columnValue);
                        }
                        results.add(row);
                    }
                }
            } else {
                int updateCount = stmt.getUpdateCount();
                System.out.println("Updated " + updateCount + " rows");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return results;
    }

    private void setParameters(PreparedStatement stmt, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            if (params[i] instanceof String) {
                stmt.setString(i + 1, (String) params[i]);
            } else if (params[i] instanceof java.sql.Date) {
                stmt.setDate(i + 1, (java.sql.Date) params[i]);
            }
            else {
                stmt.setObject(i + 1, params[i]);
            }
        }
    }

    public void printResults(List<Map<String, Object>> results) {
        for (Map<String, Object> row : results) {
            for (Map.Entry<String, Object> entry : row.entrySet()) {
                System.out.print(entry.getKey() + ": " + entry.getValue() + ", ");
            }
            System.out.println("");
        }
    }
}