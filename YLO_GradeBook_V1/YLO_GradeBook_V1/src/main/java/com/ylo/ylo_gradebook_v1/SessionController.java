package com.ylo.ylo_gradebook_v1;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public abstract class SessionController {

    public void showInfo(String title, String content) {
        showAlert(Alert.AlertType.INFORMATION, title, content);
    }

    public void showWarning(String title, String content) {
        showAlert(Alert.AlertType.WARNING, title, content);
    }

    public void showError(String title, String content) {
        showAlert(Alert.AlertType.ERROR, title, content);
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();

        Stage owner = getCurrentStage();
        if (owner != null) {
            alert.initOwner(owner);
        }

    }

    public boolean showConfirmation(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);

        ButtonType yesButton = new ButtonType("Tak", ButtonType.OK.getButtonData());
        ButtonType noButton = new ButtonType("Nie", ButtonType.CANCEL.getButtonData());
        alert.getButtonTypes().setAll(yesButton, noButton);

        Stage owner = getCurrentStage();
        if (owner != null) {
            alert.initOwner(owner);
        }

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == yesButton;
    }


    public void onLogoutButtonClicked(MouseEvent event) {
        boolean confirmed = showConfirmation("Potwierdzenie wylogowania", "Czy na pewno chcesz się wylogować?");
        if (confirmed) {
            ViewLoadingManager.loadLoginView();
        }
    }

    public String getCurrentDate() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return today.format(formatter);
    }

    private Stage getCurrentStage() {
        try {
            if (ViewLoadingManager.getMainWindow() != null) {
                return (Stage) ViewLoadingManager.getMainWindow().contentPane.getScene().getWindow();
            }
        } catch (Exception e) {
            System.err.println("Nie udało się pobrać Stage: " + e.getMessage());
        }
        return null;
    }

    public void showInfoCheckOk(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);

        Stage owner = getCurrentStage();
        if (owner != null) {
            alert.initOwner(owner);
        }

        alert.showAndWait(); // czeka na kliknięcie OK
    }



}
