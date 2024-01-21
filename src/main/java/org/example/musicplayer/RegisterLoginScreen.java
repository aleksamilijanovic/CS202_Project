package org.example.musicplayer;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.musicplayer.database.DatabaseHandler;
import org.example.musicplayer.user.User;

public class RegisterLoginScreen extends Application {

    private final DatabaseHandler databaseHandler = new DatabaseHandler();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Music Player - Register/Login");

        VBox vBox = new VBox(10);

        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();

        Button registerButton = new Button("Register");
        registerButton.setOnAction(event -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            if (!username.isEmpty() && !password.isEmpty()) {
                User user = new User(username, password);
                if(DatabaseHandler.registerUser(user)){
                    System.out.println("User registered successfully");
                    primaryStage.close();
                    openMainApplication();
                } else {
                    System.out.println("Failed to register user");
                }

            } else {
                System.out.println("Please enter both username and password for registration.");
            }
        });

        Button loginButton = new Button("Login");
        loginButton.setOnAction(event -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            if (!username.isEmpty() && !password.isEmpty()) {
                User user = new User(username, password);
                if (DatabaseHandler.loginUser(user)) {
                    System.out.println("User logged in successfully");
                    primaryStage.close();
                    openMainApplication();
                } else {
                    System.out.println("Invalid username or password. Login failed");
                }
            } else {
                System.out.println("Please enter both username and password for login.");
            }
        });

        vBox.getChildren().addAll(
                new Label("Username:"),
                usernameField,
                new Label("Password:"),
                passwordField,
                registerButton,
                loginButton
        );

        Scene scene = new Scene(vBox, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void openMainApplication() {
        MainApp mainApp = new MainApp();
        mainApp.start(new Stage());
    }

}

