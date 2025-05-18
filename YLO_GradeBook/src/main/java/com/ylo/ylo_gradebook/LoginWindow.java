package com.ylo.ylo_gradebook;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class LoginWindow {

    private boolean visible = false;

    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField passwordTextField;

    // This method is called when the "Zaloguj się" button is clicked
    @FXML
    private void onLoginClicked(ActionEvent action) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        try {
            String role = getUserRole(username, password);

            if (role == null) {
                showAlert("Błąd logowania", "Nieprawidłowa nazwa użytkownika lub hasło.");
            } else if (role.equals("TEACHER")) {
                WindowManager.openTeacherWindow();
            } else if (role.equals("STUDENT")) {
                WindowManager.openStudentWindow();
            } else {
                showAlert("Błąd", "Nieznana rola użytkownika.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Błąd", "Wystąpił błąd podczas logowania: ");
        }

    }

    //This method is called when the "Nie pamiętasz hasła" label is clicked
    @FXML
    //


    // This method retrieves the role of the user from the database if the username and password are correct
    private String getUserRole(String username, String password) throws SQLException {
        String sql = "SELECT role FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("role");
            } else {
                return null;
            }
        }


    }

    // This method shows an alert dialog with the specified title and content
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    //This method is used to reset your password
    @FXML
    private void onResetPasswordClicked(ActionEvent action) {
        String username = usernameField.getText();
        String newPassword = passwordField.getText();

    }

    //This method is used to show the password
    @FXML
    private void showPassword() {
        if (visible) {
            //hiding password
            passwordTextField.setText(passwordField.getText());
            passwordField.setVisible(true);
            passwordTextField.setVisible(false);
            visible = false;
        } else {
            //showing password
            passwordTextField.setText(passwordField.getText());
            passwordField.setVisible(false);
            passwordTextField.setVisible(true);
            visible = true;
        }

    }


}


