package org.example;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.Random;

public class DatabaseWriter {

    public static final String INSERT_QUERY = "INSERT INTO notice (message, type, processed) VALUES (?, ?, ?)";

    public static void main(String[] args) {

        try (Connection connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5440/loger_db",
                "sa", "admin")) {

            while (true) {

                String message;
                String type;
                boolean processed = false;

                if (new Random().nextBoolean()) {
                    message = "Новое сообщение от " + LocalDateTime.now();
                    type = "INFO";
                } else {
                    message = "Произошла ошибка в " + LocalDateTime.now();
                    type = "WARN";
                }

                // Prepare and execute
                try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY)) {
                    preparedStatement.setString(1, message);
                    preparedStatement.setString(2, type);
                    preparedStatement.setBoolean(3, processed);

                    preparedStatement.executeUpdate();

                    // Sleep for 1000 milliseconds
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