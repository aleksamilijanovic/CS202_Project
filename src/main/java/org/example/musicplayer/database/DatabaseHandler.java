package org.example.musicplayer.database;
import org.example.musicplayer.user.User;

import java.sql.*;

public class DatabaseHandler {

    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/mp3player";
    private static final String DATABASE_USERNAME = "aleksa";
    private static final String DATABASE_PASSWORD = "aleksa11";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
    }

    public static boolean registerUser(User user) {
        try (Connection connection = connect()){
            String query = "INSERT INTO users (username, password) VALUES (?, ?)";

            try(PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                // Set the parameters of the prepared statement
                preparedStatement.setString(1, user.getUsername());
                preparedStatement.setString(2, user.getPassword());

                preparedStatement.executeUpdate();
                System.out.println("User registered successfully");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean loginUser(User user) {
        try(Connection connection = connect()) {
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";

            try(PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, user.getUsername());
                preparedStatement.setString(2, user.getPassword());

                try(ResultSet resultSet = preparedStatement.executeQuery()){
                    if(resultSet.next()) {
                        System.out.println("User logged in successfully");
                        return true;
                    } else {
                        System.out.println("Invalid username or password. Login failed");
                        return false;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
