package com.ylo.ylo_gradebook_v1;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class LoginWindow extends SessionController implements AuthenticationInterface {

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
        String password = visible ? passwordTextField.getText() : passwordField.getText();

        try {
            String role = getUserRole(username, password);

            if (role == null) {
                showError("Błąd logowania", "Niepoprawna nazwa użytkownika lub hasło.");
            } else if (role.equals("TEACHER")) {
                ViewLoadingManager.loadTeacherView();
            } else if (role.equals("STUDENT")) {
                ViewLoadingManager.loadStudentView();
            } else {
                showError("Błąd logowania", "Nieznana rola użytkownika.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Błąd logowania", "Wystąpił błąd podczas logowania.");
        }

    }

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

    @FXML
    public void onForgotPasswordLabelClicked() {
        ViewLoadingManager.loadResetPasswordView();
    }

    //This method is used to show the password
    @Override
    public void showPassword() {
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

    @Override
    public void focusTraversableOnField(MouseEvent event) {
        usernameField.setFocusTraversable(true);
        passwordField.setFocusTraversable(true);
    }


}


