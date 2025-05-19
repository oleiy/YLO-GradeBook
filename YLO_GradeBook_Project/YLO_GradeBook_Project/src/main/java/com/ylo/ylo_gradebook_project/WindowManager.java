package com.ylo.ylo_gradebook_project;


import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;

public class WindowManager {

    public static void openLoginWindow(Stage primaryStage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(WindowManager.class.getResource("LoginWindow.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            primaryStage.setTitle("YLO GradeBook");
            primaryStage.setScene(scene);
            primaryStage.getIcons().add(new Image(WindowManager.class.getResourceAsStream("/logo.png")));
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to open the Student Window
    public static void openStudentWindow() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(WindowManager.class.getResource("StudentWindow.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage Stage = new Stage();
            Stage.setTitle("YLO GradeBook");
            Stage.setScene(scene);
            Stage.getIcons().add(new Image(WindowManager.class.getResourceAsStream("/logo.png")));
            Stage.setResizable(false);
            Stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to open the Teacher Window
    public static void openTeacherWindow() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(WindowManager.class.getResource("TeacherWindow.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage Stage = new Stage();
            Stage.setTitle("YLO GradeBook");
            Stage.setScene(scene);
            Stage.getIcons().add(new Image(WindowManager.class.getResourceAsStream("/logo.png")));
            Stage.setResizable(false);
            Stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to open the Password Reset Window
    public static void openPasswordResetWindow(Stage ownerStage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(WindowManager.class.getResource("PasswordReset.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("YLO GradeBook");
            stage.setScene(scene);
            stage.getIcons().add(new Image(WindowManager.class.getResourceAsStream("/logo.png")));
            stage.setResizable(false);

            // Obsługa zdarzenia zamknięcia okna
            stage.setOnCloseRequest(event -> {
                // Po zamknięciu okna resetu hasła otwieramy okno logowania
                Stage loginStage = new Stage();
                openLoginWindow(loginStage);
            });

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}