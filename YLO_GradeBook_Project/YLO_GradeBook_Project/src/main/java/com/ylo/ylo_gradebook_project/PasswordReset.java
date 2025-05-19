package com.ylo.ylo_gradebook_project;

import com.ylo.ylo_gradebook_project.DataBaseConnection;
import com.ylo.ylo_gradebook_project.PasswordVisible;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PasswordReset implements PasswordVisible {

    private String username;
    private String password;
    private boolean visible = false;

    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField passwordFieldConfirm;
    @FXML
    private Label passwdResetConfirmation;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField hiddenPasswordField1;
    @FXML
    private TextField hiddenPasswordField2;


    // This method is called when the "Reset Password" button is clicked
    @FXML
    private void onResetButtonClicked(ActionEvent event) {
        String username = usernameField.getText().trim();
        String newPassword = passwordField.getText();
        String confirmPassword = passwordFieldConfirm.getText();

        if (username.isEmpty()) {
            passwdResetConfirmation.setText("Proszę wprowadzić nazwę użytkownika.");
            return;
        }

        if (!doesUserExist(username)) {
            passwdResetConfirmation.setText("Użytkownik o podanej nazwie nie istnieje.");
            return;
        }

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

    @FXML
    private void updatePassword(String newPassword, String username) throws SQLException {
        String sql = "UPDATE users SET password = ? WHERE username = ?";
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newPassword);
            stmt.setString(2, username);
            stmt.executeUpdate();
        }
    }


    //This method is used to show the password
    @Override
    public void showPassword() {
        if (visible) {
            //hiding the password
            passwordField.setText(hiddenPasswordField1.getText());
            passwordField.setVisible(true);
            hiddenPasswordField1.setVisible(false);

            passwordFieldConfirm.setText(hiddenPasswordField2.getText());
            passwordFieldConfirm.setVisible(true);
            hiddenPasswordField2.setVisible(false);

            visible = false;
        } else {
            //showing the password
            hiddenPasswordField1.setText(passwordField.getText());
            hiddenPasswordField1.setVisible(true);
            passwordField.setVisible(false);

            hiddenPasswordField2.setText(passwordFieldConfirm.getText());
            hiddenPasswordField2.setVisible(true);
            passwordFieldConfirm.setVisible(false);

            visible = true;
        }
    }

    // This method checks if the user exists in the database
    private boolean doesUserExist(String username) {
        String sql = "SELECT 1 FROM users WHERE username = ?";
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            return stmt.executeQuery().next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


}
