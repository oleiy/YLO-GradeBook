package com.ylo.ylo_gradebook_v1;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import models.Classes;
import models.Users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class LoginWindow extends SessionController implements AuthenticationInterface {

    private boolean visible = false;


    //-------------------- FXML for LoginWindow --------------------
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField passwordTextField;

    //------------------------------------------------------------------------------------------------------
    // This method is called when the "Zaloguj się" button is clicked
    //------------------------------------------------------------------------------------------------------
    @FXML
    private void onLoginClicked(ActionEvent action) {
        String username = usernameField.getText();
        String password = visible ? passwordTextField.getText() : passwordField.getText();

        try {
            Users user = getUserData(username, password);

            if (user == null) {
                showError("Błąd logowania", "Niepoprawna nazwa użytkownika lub hasło.");
            } else {
                Session.setLoggedUser(user);

                if (user.getRole().equals("TEACHER")) {
                    ViewLoadingManager.loadTeacherView();
                } else if (user.getRole().equals("STUDENT")) {
                    ViewLoadingManager.loadStudentView();
                } else {
                    showError("Błąd logowania", "Nieznana rola użytkownika.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Błąd logowania", "Wystąpił błąd podczas logowania.");
        }
    }

    //------------------------------------------------------------------------------------------------------
    // This method retrieves the role of the user from the database if the username and password are correct
    //------------------------------------------------------------------------------------------------------
    private Users getUserData(String username, String password) throws SQLException {
        String sql = "SELECT id, username, password, first_name, last_name, role, class_id FROM users WHERE username = ? AND password = ?";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int classId = rs.getInt("class_id");
                Classes userClass = new Classes(classId);

                return new Users(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("role"),
                        userClass
                );
            } else {
                return null;
            }
        }
    }

    // This method is called when the "Zapomniałeś hasła" label is clicked
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

    // Method for enabling field focus on mouse click
    @Override
    public void enableFieldFocus(MouseEvent event) {
        usernameField.setFocusTraversable(true);
        passwordField.setFocusTraversable(true);
    }


}


