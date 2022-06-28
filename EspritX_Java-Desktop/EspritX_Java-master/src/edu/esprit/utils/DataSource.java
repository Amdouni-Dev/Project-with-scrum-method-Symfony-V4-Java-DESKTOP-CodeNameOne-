package edu.esprit.utils;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.*;
import java.util.logging.Logger;

public class DataSource {
    private static Connection cnx = null;
    private static final transient Logger log;

    static {
        log = Logger.getLogger(DataSource.class.getName());
    }
    public static Connection getConnection() {
        if (cnx == null) {
            Dotenv dotenv = Dotenv.load();
            String JDBC_URL = dotenv.get("JDBC_URL");
            String JDBC_USER = dotenv.get("JDBC_USER");
            String JDBC_PASSWORD = dotenv.get("JDBC_PASSWORD");
            try {
                cnx = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD); //it's pointless to catch and rethrow. Just corrupts stacktrace.
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return cnx;
    }

    public static ResultSet executeQuery(String query) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(query);
            log.info("Executing query: " + preparedStatement.toString());
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Should never happen");
    }

    public static int executeUpdate(String dml) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(dml);
            log.info("Executing update: " + preparedStatement.toString());
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; //should never happen
    }
}
