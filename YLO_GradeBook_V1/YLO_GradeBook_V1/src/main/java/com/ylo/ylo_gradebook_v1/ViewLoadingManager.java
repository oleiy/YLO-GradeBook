package com.ylo.ylo_gradebook_v1;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ViewLoadingManager {

    private static MainWindow mainWindow;

    // Method to open the main window and load the MainWindow.fxml
    public static void openMainWindow(Stage primaryStage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(ViewLoadingManager.class.getResource("MainWindow.fxml"));
            Parent root = fxmlLoader.load();
            mainWindow = fxmlLoader.getController();
            Scene scene = new Scene(root);
            primaryStage.setTitle("YLO GradeBook");
            primaryStage.setScene(scene);
            primaryStage.getIcons().add(new Image(ViewLoadingManager.class.getResourceAsStream("/logo.png")));
            primaryStage.setResizable(false);
            primaryStage.centerOnScreen();
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to load the PasswordResetView into the main window
    public static void loadResetPasswordView() {
        try {
            if (mainWindow == null) {
                System.err.println("ERROR: mainWindow controller is null! Can't load ResetPasswordView.");
                return;
            }

            FXMLLoader loader = new FXMLLoader(ViewLoadingManager.class.getResource("PasswordReset.fxml"));
            AnchorPane resetPasswordView = loader.load();
            resetPasswordView.setPrefWidth(568);
            resetPasswordView.setPrefHeight(640);
            mainWindow.contentPane.getChildren().setAll(resetPasswordView);
            Stage stage = (Stage) mainWindow.contentPane.getScene().getWindow();
            stage.sizeToScene();
            stage.centerOnScreen();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to load the LoginView into the main window
    public static void loadLoginView() {
        try {
            if (mainWindow == null) {
                System.err.println("ERROR: mainWindow controller is null! Can't load LoginView.");
                return;
            }

            FXMLLoader loader = new FXMLLoader(ViewLoadingManager.class.getResource("LoginWindow.fxml"));
            AnchorPane loginView = loader.load();
            loginView.setPrefWidth(568);
            loginView.setPrefHeight(600);
            mainWindow.contentPane.getChildren().setAll(loginView);
            Stage stage = (Stage) mainWindow.contentPane.getScene().getWindow();
            stage.sizeToScene();
            stage.centerOnScreen();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //Loads the StudentView into the main window.
    public static void loadStudentView() {
        try {
            if (mainWindow == null) {
                System.err.println("ERROR: mainWindow controller is null! Can't load StudentView.");
                return;
            }

            FXMLLoader loader = new FXMLLoader(ViewLoadingManager.class.getResource("StudentWindow.fxml"));
            AnchorPane studentView = loader.load();
            studentView.setPrefWidth(1280);
            studentView.setPrefHeight(720);
            mainWindow.contentPane.getChildren().setAll(studentView);
            Stage stage = (Stage) mainWindow.contentPane.getScene().getWindow();
            stage.sizeToScene();
            stage.centerOnScreen();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    // Loads the TeacherView into the main window.
    public static void loadTeacherView() {
        try {
            if (mainWindow == null) {
                System.err.println("ERROR: mainWindow controller is null! Can't load TeacherView.");
                return;
            }

            FXMLLoader loader = new FXMLLoader(ViewLoadingManager.class.getResource("TeacherWindow.fxml"));
            AnchorPane teacherView = loader.load();
            teacherView.setPrefWidth(1280);
            teacherView.setPrefHeight(720);
            mainWindow.contentPane.getChildren().setAll(teacherView);
            Stage stage = (Stage) mainWindow.contentPane.getScene().getWindow();
            stage.sizeToScene();
            stage.centerOnScreen();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static MainWindow getMainWindow() {
        return mainWindow;
    }

}
