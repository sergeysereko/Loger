package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseReader {

    public static final String SELECT_QUERY = "SELECT id, message, type, processed FROM notice WHERE type = 'INFO' AND processed = false";
    public static final String DELETE_QUERY = "DELETE FROM notice WHERE id = ?";

    public static void main(String[] args) {

        try (Connection connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5440/loger_db",
                "sa", "admin")) {


            while (true) {

                try (PreparedStatement selectStatement = connection.prepareStatement(SELECT_QUERY);
                     ResultSet resultSet = selectStatement.executeQuery()) {


                    while (resultSet.next()) {
                        int id = resultSet.getInt("id");
                        String message = resultSet.getString("message");


                        System.out.println("Message: " + message);

                        //DELETE
                        try (PreparedStatement deleteStatement = connection.prepareStatement(DELETE_QUERY)) {
                            deleteStatement.setInt(1, id);
                            deleteStatement.executeUpdate();
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