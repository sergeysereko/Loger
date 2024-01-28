package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseWarnReader {

    public static final String SELECT_QUERY = "SELECT id, message, type, processed FROM notice WHERE type = 'WARN' AND processed = false";
    public static final String UPDATE_QUERY = "UPDATE notice SET processed = true WHERE id = ?";

    public static void main(String[] args) {

        try (Connection connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5440/loger_db",
                "sa", "admin")) {


            while (true) {
                // Execute the SELECT query
                try (PreparedStatement selectStatement = connection.prepareStatement(SELECT_QUERY);
                     ResultSet resultSet = selectStatement.executeQuery()) {


                    while (resultSet.next()) {
                        int id = resultSet.getInt("id");
                        String message = resultSet.getString("message");


                        System.out.println("Message: " + message);

                        // Execute the UPDATE query for the current id
                        try (PreparedStatement updateStatement = connection.prepareStatement(UPDATE_QUERY)) {
                            updateStatement.setInt(1, id);
                            updateStatement.executeUpdate();
                        }
                    }


                    Thread.sleep(1000);

                } catch (SQLException | InterruptedException e) {
                    e.printStackTrace();
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}