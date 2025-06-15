package com.ylo.ylo_gradebook_v1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PasswordReset extends SessionController implements AuthenticationInterface {

    private boolean visible = false;

    //--------------- FXML for PasswordReset --------------------
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

    //------------------------------------------------------------------------------------------------------
    //--------------- This method is called when the "Reset Password" button is clicked --------------------
    //------------------------------------------------------------------------------------------------------
    @FXML
    private void onResetButtonClicked(ActionEvent event) {
        String username = usernameField.getText().trim();
        String newPassword = visible ? hiddenPasswordField1.getText() : passwordField.getText();
        String confirmPassword = visible ? hiddenPasswordField2.getText() : passwordFieldConfirm.getText();
        if (username.isEmpty()) {
            showWarning("Brak nazwy użytkownika", "Wprowadź nazwę użytkownika, aby zresetować hasło.");
            return;
        }
        if (!doesUserExist(username)) {
            showError("Nie znaleziono użytkownika", "Podany użytkownik nie istnieje.");
            return;
        }
        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
            showWarning("Brak hasła", "Wypełnij oba pola hasła.");
            return;
        }
        if (!newPassword.equals(confirmPassword)) {
            showError("Nieprawidłowe hasła", "Hasła nie są identyczne.");
            return;
        }

        boolean confirmed = showConfirmation(
                "Potwierdzenie resetowania hasła",
                "Czy na pewno chcesz zresetować hasło?\nPo zmianie będziesz musiał zalogować się ponownie."
        );

        if (!confirmed) {
            showInfo("Anulowano", "Resetowanie hasła zostało anulowane.");
            return;
        }
        try {
            updatePassword(newPassword, username);
            showInfoCheckOk("Sukces", "Hasło zostało zaktualizowane pomyślnie.");
            ViewLoadingManager.loadLoginView();
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Błąd bazy danych", "Nie udało się zaktualizować hasła.");
        }
    }

    @FXML
    private void updatePassword(String newPassword, String username) throws SQLException {
        String sql = "UPDATE users SET password = ? WHERE username = ?";
        try (Connection conn = com.ylo.ylo_gradebook_v1.DataBaseConnection.getConnection();
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

    //  Method for enabling field focus on mouse click
    @Override
    public void enableFieldFocus(MouseEvent event) {
        usernameField.setFocusTraversable(true);
        passwordField.setFocusTraversable(true);
        passwordFieldConfirm.setFocusTraversable(true);
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

    //------------------------------------------------------------------------------------------------------
    //-------------------- This method is called when the "Cancel" label is clicked ------------------------
    //------------------------------------------------------------------------------------------------------
    public void onCancelLabelClicked(MouseEvent event) {
        boolean confirmed = showConfirmation("Anulowanie", "Czy na pewno chcesz anulować resetowanie hasła?");
        if (confirmed) {
            ViewLoadingManager.loadLoginView();
        }
    }

}
