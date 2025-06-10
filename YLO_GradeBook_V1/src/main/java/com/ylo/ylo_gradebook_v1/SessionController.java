package com.ylo.ylo_gradebook_v1;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.util.Optional;

public abstract class SessionController {

    //-----------------------------------------------------------------
    // -------------------- Method for open popup window --------------------
    //-----------------------------------------------------------------
    public static void openPopUpWindow(String paneId) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(ViewLoadingManager.class.getResource("PopUpWindow.fxml"));
            Parent root = fxmlLoader.load();
            PopUpWindow controller = fxmlLoader.getController();
            controller.showPane(paneId);
            Stage popupStage = new Stage();
            popupStage.setTitle("YLO GradeBook");
            popupStage.setScene(new Scene(root));
            popupStage.getIcons().add(new Image(ViewLoadingManager.class.getResourceAsStream("/logo.png")));
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setResizable(false);
            popupStage.centerOnScreen();
            popupStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //-----------------------------------------------------------------
    // -------------------- Methods for errors --------------------
    //-----------------------------------------------------------------

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

    //-----------------------------------------------------------------
    // -------------------- Method for logout --------------------
    //-----------------------------------------------------------------

    public void onLogoutButtonClicked(MouseEvent event) {
        boolean confirmed = showConfirmation("Potwierdzenie wylogowania", "Czy na pewno chcesz się wylogować?");
        if (confirmed) {
            ViewLoadingManager.loadLoginView();
        }
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

    public void addNewPersonalNoteButton() {
        //
    }


}
