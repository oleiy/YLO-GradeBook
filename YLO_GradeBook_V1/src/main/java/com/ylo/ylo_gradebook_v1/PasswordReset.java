package com.ylo.ylo_gradebook_v1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PasswordReset extends BaseAlert implements com.ylo.ylo_gradebook_v1.PasswordVisible {

    private boolean visible = false;

    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField passwordFieldConfirm;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField hiddenPasswordField1;
    @FXML
    private TextField hiddenPasswordField2;
    private Stage primaryStage;


    // This method is called when the "Reset Password" button is clicked
    @FXML
    private void onResetButtonClicked(ActionEvent event) {
        String username = usernameField.getText().trim();
        String newPassword = visible ? hiddenPasswordField1.getText() : passwordField.getText();
        String confirmPassword = visible ? hiddenPasswordField2.getText() : passwordFieldConfirm.getText();

        if (username.isEmpty()) {
            BaseAlert.showWarning("Brak nazwy użytkownika", "Wprowadź nazwę użytkownika, aby zresetować hasło.");
            return;
        }

        if (!doesUserExist(username)) {
            BaseAlert.showError("Nie znaleziono użytkownika", "Podany użytkownik nie istnieje.");
            return;
        }

        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
            BaseAlert.showWarning("Brak hasła", "Wypełnij oba pola hasła.");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            BaseAlert.showError("Nieprawidłowe hasła", "Hasła nie są identyczne.");
            return;
        }

        try {
            updatePassword(newPassword, username);
            BaseAlert.showInfo("Sukces", "Hasło zostało zaktualizowane.");
            ViewLoadingManager.loadLoginView();
        } catch (SQLException e) {
            e.printStackTrace();
            BaseAlert.showError("Błąd bazy danych", "Nie udało się zaktualizować hasła.");
        }
    }

    @FXML
    private void updatePassword(String newPassword, String username) throws SQLException {
        String sql = "UPDATE users SET password = ? WHERE username = ?";
        try (Connection conn =  com.ylo.ylo_gradebook_v1.DataBaseConnection.getConnection();
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
        try (Connection conn = com.ylo.ylo_gradebook_v1.DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            return stmt.executeQuery().next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


}
