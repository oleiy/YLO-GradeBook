package com.ylo.ylo_gradebook;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PasswordReset {

    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField passwordFieldConfirm;
    @FXML
    private Label passwdResetConfirmation;
    @FXML
    private TextField usernameField;


    // This method is called when the "Reset Password" button is clicked
    @FXML
    private void onResetPasswordClicked(ActionEvent event) {
        String username = usernameField.getText();
        String newPassword = passwordField.getText();
        String confirmPassword = passwordFieldConfirm.getText();


        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
            passwdResetConfirmation.setText("Proszę wprowadzić nowe hasło.");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            passwdResetConfirmation.setText("Hasła nie pasują do siebie.");
            return;
        }

        try {
            updatePassword(newPassword, username);
            passwdResetConfirmation.setText("Hasło zostało zaktualizowane pomyślnie.");
        } catch (SQLException e) {
            e.printStackTrace();
            passwdResetConfirmation.setText("Wystąpił błąd podczas aktualizacji hasła.");
        }

    }

    private void updatePassword(String newPassword, String username) throws SQLException {
        String sql = "UPDATE users SET password = ? WHERE username = ?";
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newPassword);
            stmt.setString(2, username);
            stmt.executeUpdate();
        }
    }


}
