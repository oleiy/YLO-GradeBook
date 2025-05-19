package com.ylo.ylo_gradebook_v1;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ViewLoadingManager {

    private static MainWindow mainWindow;

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
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void loadResetPasswordView() {
        try {
            if (mainWindow == null) {
                System.err.println("ERROR: mainWindow controller is null! Can't load ResetPasswordView.");
                return;
            }

            FXMLLoader loader = new FXMLLoader(ViewLoadingManager.class.getResource("PasswordReset.fxml"));
            AnchorPane resetPasswordView = loader.load();
            resetPasswordView.setPrefWidth(568);
            resetPasswordView.setPrefHeight(660);
            mainWindow.contentPane.getChildren().setAll(resetPasswordView);
            Stage stage = (Stage) mainWindow.contentPane.getScene().getWindow();
            stage.sizeToScene();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadStudentView() {
        try {
            if (mainWindow == null) {
                System.err.println("ERROR: mainWindow controller is null! Can't load StudentView.");
                return;
            }

            FXMLLoader loader = new FXMLLoader(ViewLoadingManager.class.getResource("StudentView.fxml"));
            AnchorPane studentView = loader.load();
            studentView.setPrefWidth(1280);
            studentView.setPrefHeight(720);
            mainWindow.contentPane.getChildren().setAll(studentView);
            Stage stage = (Stage) mainWindow.contentPane.getScene().getWindow();
            stage.sizeToScene();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void loadTeacherView() {
        try {
            if (mainWindow == null) {
                System.err.println("ERROR: mainWindow controller is null! Can't load TeacherView.");
                return;
            }

            FXMLLoader loader = new FXMLLoader(ViewLoadingManager.class.getResource("TeacherView.fxml"));
            AnchorPane teacherView = loader.load();
            teacherView.setPrefWidth(1280);
            teacherView.setPrefHeight(720);
            mainWindow.contentPane.getChildren().setAll(teacherView);
            Stage stage = (Stage) mainWindow.contentPane.getScene().getWindow();
            stage.sizeToScene();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
